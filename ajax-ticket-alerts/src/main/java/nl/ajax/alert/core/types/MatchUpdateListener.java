package nl.ajax.alert.core.types;

import nl.ajax.alert.api.MatchDTO;

public interface MatchUpdateListener {
    void onMatchUpdate(MatchDTO matchDTO);
}
