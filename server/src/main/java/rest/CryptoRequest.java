package rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoRequest {
    CreditDebitIndicator indicator;
    int val;
    CryptoType cryptoType;
}
