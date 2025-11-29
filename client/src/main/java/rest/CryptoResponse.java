package rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoResponse {
    private int val;
    private CryptoType cryptoType;
    private CreditDebitIndicator creditDebitIndicator;
}
