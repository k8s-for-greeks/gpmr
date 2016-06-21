package chrislovecnm.k8s.gpmr.repository;

/**
 * Created by clove on 6/1/16.
 */

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  *
 *  * The solution of skipping rows is that use page state rather than iterator
 *  * rows one by one.
 *  *
 *  
 */
@Repository
public class CassandraPaging {

    @Inject
    protected Session session;

    protected Statement findAllStmtPaging;

    protected PreparedStatement findAllStmt;

    protected PreparedStatement truncateStmt;

    private String keyspace;

    private String tableName;

    private final Logger log = LoggerFactory.getLogger(CassandraPaging.class);


    public CassandraPaging() {

    }

    protected void createPaging(String keyspace, String tableName) {
        this.keyspace = keyspace;
        this.tableName = tableName;
        findAllStmtPaging = QueryBuilder.select().from(keyspace, tableName);
        findAllStmt = session.prepare("SELECT * FROM " + tableName);
        truncateStmt = session.prepare("TRUNCATE " + tableName);
    }

    /**
     * Retrieve rows for the specified page offset.
     *
     * @param pageable
     *            {@link Pageable}
     * @return List<Row>
     */
    protected List<Row> fetchRowsWithPage(Pageable pageable) {
        log.debug("Pageable.getPageNumber: {}", pageable.getPageNumber());
        log.debug("Pageable.getPageSize: {}", pageable.getPageSize());

        Statement findAllStmtPagingFetch = QueryBuilder.select().from(keyspace, tableName);
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() : 1;
        int pageSize =  pageable.getPageSize() > 0 ? pageable.getPageSize() : 100;

        ResultSet result = skipRows(findAllStmtPagingFetch, pageNumber, pageSize);
        return getRows(result,pageNumber,pageSize);
    }

    private ResultSet skipRows(Statement statement, int pageNumber, int pageSize) {
        ResultSet result = null;
        int skippingPages = pageNumber;
        String savingPageState = null;
        statement.setFetchSize(pageSize);
        boolean isEnd = false;
        for (int i = 0; i < skippingPages; i++) {
            log.debug("skipping pages");
            if (null != savingPageState) {
                statement = statement.setPagingState(PagingState
                    .fromString(savingPageState));
            }
            result = session.execute(statement);
            PagingState pagingState = result.getExecutionInfo()
                .getPagingState();
            if (null != pagingState) {
                savingPageState = result.getExecutionInfo().getPagingState()
                    .toString();
            }

            if (result.isFullyFetched() && null == pagingState) {
                // if hit the end more than once, then nothing to return,
                // otherwise, mark the isEnd to 'true'
                if (isEnd) {
                    return null;
                } else {
                    isEnd = true;
                }
            }
        }
        return result;
    }

    private List<Row> getRows(ResultSet result, int pageNumber, int pageSize) {

        List<Row> rows = new ArrayList<>(pageSize);
        if (null == result) {
            return rows;
        }

        int start = (pageNumber - 1) * pageSize;
        int skippingRows = start % pageSize;
        int index = 0;
        for (Iterator<Row> iter = result.iterator(); iter.hasNext() && rows.size() < pageSize;) {
            Row row = iter.next();
            if (index >= skippingRows) {
                rows.add(row);
            }
            index++;
        }
        return rows;
    }


}
