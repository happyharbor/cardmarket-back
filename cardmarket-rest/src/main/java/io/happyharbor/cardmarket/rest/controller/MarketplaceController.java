package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.dto.market.GetExpansionsRequest;
import io.happyharbor.cardmarket.api.dto.market.GetExpansionsResponse;
import io.happyharbor.cardmarket.api.dto.market.GetGamesResponse;
import io.happyharbor.cardmarket.api.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marketplace")
@Log4j2
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @GetMapping(value = "get-product-list", produces = "application/zip")
    @PreAuthorize("hasRole('ROLE_USER')")
    public byte[] getProductList() {
        return marketplaceService.getProductList().join();
    }

    @GetMapping(value = "games")
    @PreAuthorize("hasRole('ROLE_USER')")
    public GetGamesResponse getGames() {
        return marketplaceService.getGames().join();
    }

    @GetMapping(value = "games/{gameId}/expansions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public GetExpansionsResponse getGames(@PathVariable int gameId) {
        return marketplaceService.getExpansions(GetExpansionsRequest.builder().gameId(gameId).build()).join();
    }
}
