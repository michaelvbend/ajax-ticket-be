package nl.ajax.alert.core.types;

import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.db.models.Match;

import java.util.List;

public interface MatchUpdateListener {
    public void onMatchUpdate(MatchDTO matchDTO);
}
