package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface BatchUpdateHandler {
    public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException;
    public void handleGeneratedIds(ResultSet rs, int count) throws SQLException;
}
