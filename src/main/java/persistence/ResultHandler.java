package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler {
    public void handle(ResultSet rs) throws SQLException;
}
