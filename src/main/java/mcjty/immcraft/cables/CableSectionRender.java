package mcjty.immcraft.cables;


import mcjty.immcraft.varia.Vector;

/**
 * Cable section for the renderer (BundleISBM)
 */
public class CableSectionRender {
    private final CableSubType subType;
    private final Vector vector;
    private final Vector vector1;
    private final Vector vector2;

    public CableSectionRender(CableSubType subType, Vector vector, Vector vector1, Vector vector2) {
        this.subType = subType;
        this.vector = vector;
        this.vector1 = vector1;
        this.vector2 = vector2;
    }

    public CableSubType getSubType() {
        return subType;
    }

    public Vector getVector() {
        return vector;
    }

    public Vector getVector1() {
        return vector1;
    }

    public Vector getVector2() {
        return vector2;
    }
}