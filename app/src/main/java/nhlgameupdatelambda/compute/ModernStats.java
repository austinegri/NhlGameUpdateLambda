package nhlgameupdatelambda.compute;

import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.exception.DataProcessingException;

public interface ModernStats {
    void compute(final NhlData nhlData) throws DataProcessingException;
}
