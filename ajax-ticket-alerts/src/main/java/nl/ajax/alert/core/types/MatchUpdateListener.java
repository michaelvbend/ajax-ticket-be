package nl.ajax.alert.core.types;

import nl.ajax.alert.api.MatchDTO;

import java.util.List;

public interface MatchUpdateListener {
    public void onMatchUpdate(List<MatchDTO> matchDTO);
}
