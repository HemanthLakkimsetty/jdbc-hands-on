import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(int id, int accountId, String type, BigDecimal amount, LocalDateTime time) { }
