package io.happyharbor.cardmarket.api.dto.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.happyharbor.cardmarket.api.dto.order.Address;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Account {
    @JsonAlias("idUser")
    String userId;
    String username;
    int isCommercial;
    int reputation;
    long sellCount;
    boolean onVacation;
    String country;
    boolean maySell;
    int sellerActivation;
    int shipsFast;
    long soldItems;
    int avgShippingTime;
    int idDisplayLanguage;
    UserName name;
    Address homeAddress;
    String email;
    String phoneNumber;
    String vat;
    String legalInformation;
    LocalDateTime registerDate;
    @JsonAlias("isActivated")
    boolean activated;
    BankAccount bankAccount;
    int articlesInShoppingCart;
    int unreadMessages;
    MoneyDetails moneyDetails;
    int riskGroup;
    String lossPercentage;
}
