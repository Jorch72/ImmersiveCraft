package mcjty.immcraft.cables;

import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.varia.IntersectionTools;
import mcjty.immcraft.api.util.Vector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class CableRenderer {

    public static CableSection findSelectedCable(Vector player, Vector hitVec, BundleTE bundleTE) {
        CableSection closestSection = null;
        float mindist = 1000000.0f;
        for (CableSection section : bundleTE.getCableSections()) {
            Vector vector = section.getVector();
            Vector vector1 = section.getVector(0);
            if (vector1 == null) {
                vector1 = new Vector(vector.getX() + .2f, vector.getY() + .2f, vector.getZ() + .2f);
            }
            Vector vector2 = section.getVector(1);

            float dist = IntersectionTools.calculateRayToLineDistance(player, Vector.subtract(hitVec, player), vector, vector1);
            if (dist < mindist) {
                mindist = dist;
                closestSection = section;
            }
            if (vector2 != null) {
                dist = IntersectionTools.calculateRayToLineDistance(player, Vector.subtract(hitVec, player), vector, vector2);
                if (dist < mindist) {
                    mindist = dist;
                    closestSection = section;
                }
            }
        }
        return closestSection;
    }

    public static void renderHilightedCable(Vector player, CableSection section) {
//        Vector vector = section.getVector();
//        Vector vector1 = section.getVector(0);
//        if (vector1 == null) {
//            vector1 = new Vector(vector.getX() + .2f, vector.getY() + .2f, vector.getZ() + .2f);
//        }
//        Vector vector2 = section.getVector(1);
//
//        GL11.glEnable(GL11.GL_BLEND);
//        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
//        GL11.glLineWidth(2.0F);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDepthMask(false);
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawing(GL11.GL_LINES);
//        tessellator.setColorOpaque_I(0xffffffff);
//
//        RenderHelper.drawBeamLines(Vector.subtract(vector, player), Vector.subtract(vector1, player), new Vector(0, 0, 0), .07f);
//        if (vector2 != null) {
//            RenderHelper.drawBeamLines(Vector.subtract(vector, player), Vector.subtract(vector2, player), new Vector(0, 0, 0), .07f);
//        }
//        tessellator.draw();
//
//        GL11.glDepthMask(true);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void renderCable(Vector player, CableSection section) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        Vector vector = section.getVector();
        Vector vector1 = section.getVector(0);
        if (vector1 == null) {
            vector1 = new Vector(vector.getX() + .2f, vector.getY() + .2f, vector.getZ() + .2f);
        }
        Vector vector2 = section.getVector(1);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        drawBeamExtended(player, vector, vector1);
        if (vector2 != null) {
            drawBeamExtended(player, vector, vector2);
        }
        tessellator.draw();
    }

    private static void drawBeamExtended(Vector player, Vector v1, Vector v2) {
        // @todo optimize this by precalculating?
        Vector diff = Vector.mul(Vector.subtract(v2, v1).normalize(), .02f);
//        RenderHelper.drawBeam(Vector.subtract(v1, diff), Vector.add(v2, diff), player, .05f);
    }
}
