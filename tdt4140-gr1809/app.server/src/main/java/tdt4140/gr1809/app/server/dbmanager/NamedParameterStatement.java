package tdt4140.gr1809.app.server.dbmanager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NamedParameterStatement {
    private PreparedStatement statement;
    private Map<String, Integer> parameterIndexMap;

    public NamedParameterStatement(String statement, Connection conn) throws SQLException {
        parameterIndexMap = new HashMap<>();
        StringBuilder preparedStatement = new StringBuilder();
        int i0 = 0, i1 = -1;
        int paramNumber = 1;
        while ((i0 = statement.indexOf(':', i1 + 1)) > 0) {
            preparedStatement.append(statement.subSequence(i1 + 1, i0));
            preparedStatement.append('?');
            i1 = statement.indexOf(':', i0 + 1);
            String parameter = statement.substring(i0 + 1, i1);
            parameterIndexMap.put(parameter, paramNumber++);
        }

        preparedStatement.append(statement.subSequence(i1 + 1, statement.length()));

        this.statement = conn.prepareStatement(preparedStatement.toString());
    }

    public void setInt(String parameter, Integer value) throws SQLException {
        statement.setObject(parameterIndexMap.get(parameter), value, Types.INTEGER);
    }

    public void setBoolean(String parameter, Boolean value) throws SQLException {
        statement.setObject(parameterIndexMap.get(parameter), value, Types.BOOLEAN);
    }

    public void setString(String parameter, String value) throws SQLException {
        statement.setString(parameterIndexMap.get(parameter), value);
    }

    public void setTimestamp(String parameter, Timestamp value) throws SQLException {
        statement.setTimestamp(parameterIndexMap.get(parameter), value);
    }

    public void setTimestamp(String parameter, LocalDateTime value) throws SQLException {
        if (Objects.isNull(value)) {
            statement.setNull(parameterIndexMap.get(parameter), Types.TIMESTAMP);
        } else {
            Timestamp timestamp = Timestamp.valueOf(value);
            setTimestamp(parameter, timestamp);
        }
    }

    public PreparedStatement getStatement() {
        return statement;
    }
}
