package rest;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoRequest {
    CreditDebitIndicator indicator;
    int val;
    CryptoType cryptoType;
}
