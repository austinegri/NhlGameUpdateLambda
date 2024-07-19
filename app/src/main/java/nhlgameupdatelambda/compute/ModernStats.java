package nhlgameupdatelambda.compute;

import nhlgameupdatelambda.data.NhlData;

public interface ModernStats {
    void compute(final NhlData nhlData);
}
