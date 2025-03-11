package nl.ajax.alert.api;

public record Match(Long id, String homeTeam, String awayTeam, boolean soldOut, String matchLink) {
}