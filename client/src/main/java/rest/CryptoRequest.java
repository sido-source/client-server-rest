package rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoRequest {
    CreditDebitIndicator indicator;
    int val;
    CryptoType cryptoType;
}
