package hr.hrsak.bankingdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hr.hrsak.bankingdemo.models.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    @JsonProperty("sender_account_id")
    private int senderAccountId;
    @JsonProperty("receiver_account_id")
    private int receiverAccountId;
    @JsonProperty("currency_id")
    private CurrencyEnum currencyId;
    private BigDecimal amount;
    private String message;
    @JsonProperty("time_stamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timeStamp;
}
