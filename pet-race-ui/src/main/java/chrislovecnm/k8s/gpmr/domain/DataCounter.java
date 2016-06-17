package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DataCounter.
 */

@Table(name = "data_counter")
public class DataCounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private String vtype;

    private Long value;

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataCounter dataCounter = (DataCounter) o;
        if(dataCounter.getVtype() == null || vtype == null) {
            return false;
        }
        return Objects.equals(vtype, dataCounter.getVtype());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vtype);
    }

    @Override
    public String toString() {
        return "DataCounter{" +
            "vtype='" + vtype + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
