package chrislovecnm.k8s.gpmr.repository;

/**
 * Created by clove on 6/1/16.
 */

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
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

    protected String tableName;

    protected String keySpace;

    protected Statement findAllStmtPaging;

    protected PreparedStatement findAllStmt;

    protected PreparedStatement truncateStmt;

    public CassandraPaging() {

    }

    protected void createPaging(Mapper mapper, String keySpace, String tableName) {
        this.tableName = tableName;
        this.keySpace = keySpace;
        findAllStmtPaging = QueryBuilder.select().from(keySpace, tableName);
        findAllStmt = session.prepare("SELECT * FROM " + tableName);
        truncateStmt = session.prepare("TRUNCATE " + tableName);
    }

    /**
     * Retrieve rows for the specified page offset.
     *
     * @param start
     *            starting row (>1), inclusive
     * @param size
     *            the maximum rows need to retrieve.
     * @return List<Row>
     */
    public List<Row> fetchRowsWithPage(int start, int size) {
        if(start == 0) start = 1;
        ResultSet result = skipRows(findAllStmtPaging, start, size);
        return getRows(result, start, size);
    }

    private ResultSet skipRows(Statement statement, int start, int size) {
        ResultSet result = null;
        int skippingPages = getPageNumber(start, size);
        String savingPageState = null;
        statement.setFetchSize(size);
        boolean isEnd = false;
        for (int i = 0; i < skippingPages; i++) {
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
                if (true == isEnd) {
                    return null;
                } else {
                    isEnd = true;
                }
            }
        }
        return result;
    }

    private int getPageNumber(int start, int size) {
        if (start < 1) {
            throw new IllegalArgumentException(
                "Starting row need to be larger than 1");
        }
        int page = 1;
        if (start > size) {
            page = (start - 1) / size + 1;
        }
        return page;
    }

    private List<Row> getRows(ResultSet result, int start, int size) {
        List<Row> rows = new ArrayList<>(size);
        if (null == result) {
            return rows;
        }
        int skippingRows = (start - 1) % size;
        int index = 0;
        for (Iterator<Row> iter = result.iterator(); iter.hasNext()
            && rows.size() < size;) {
            Row row = iter.next();
            if (index >= skippingRows) {
                rows.add(row);
            }
            index++;
        }
        return rows;
    }

}
