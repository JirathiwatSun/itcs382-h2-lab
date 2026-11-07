package parcelpoint.storage;

import parcelpoint.domain.Parcel;
import parcelpoint.domain.ParcelSize;
import parcelpoint.domain.ParcelStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class H2ParcelRepository implements ParcelRepository {
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS PARCEL (
                CODE          VARCHAR(20) PRIMARY KEY,
                STUDENT_ID    VARCHAR(20) NOT NULL,
                PARCEL_SIZE   VARCHAR(10) NOT NULL,
                STATUS        VARCHAR(10) NOT NULL,
                LOCKER_NUMBER INT
            )""";

    private final String url;

    public H2ParcelRepository(String path) {
        url = "jdbc:h2:" + path;
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create the PARCEL table", e);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, "sa", "");
    }

    @Override
    public void save(Parcel parcel) {
        String sql = "MERGE INTO PARCEL KEY (CODE) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parcel.getCode());
            statement.setString(2, parcel.getStudentId());
            statement.setString(3, parcel.getSize().name());
            statement.setString(4, parcel.getStatus().name());
            statement.setObject(5, parcel.getLockerNumber(), Types.INTEGER);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save parcel " + parcel.getCode(), e);
        }
    }

    @Override
    public Optional<Parcel> findByCode(String code) {
        String sql = "SELECT * FROM PARCEL WHERE CODE = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet row = statement.executeQuery()) {
                return row.next() ? Optional.of(toParcel(row)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read parcel " + code, e);
        }
    }

    @Override
    public List<Parcel> findAll() {
        // TODO Step 0.11: read every row of the PARCEL table as findByCode does,
        // TODO turn each row into a Parcel with toParcel, and return the list.
        return List.of();
    }

    private Parcel toParcel(ResultSet row) throws SQLException {
        return new Parcel(
                row.getString("CODE"),
                row.getString("STUDENT_ID"),
                ParcelSize.valueOf(row.getString("PARCEL_SIZE")),
                ParcelStatus.valueOf(row.getString("STATUS")),
                row.getObject("LOCKER_NUMBER", Integer.class));
    }
}
