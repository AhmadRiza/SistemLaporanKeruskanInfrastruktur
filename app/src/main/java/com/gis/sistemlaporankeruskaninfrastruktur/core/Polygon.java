package com.gis.sistemlaporankeruskaninfrastruktur.core;

import java.util.ArrayList;
import java.util.List;


public class Polygon {
    /* Create Polygon for Each Area in Malang */

    public static List<Region> regionList = new ArrayList<>();

    /* ====================================== */
    private final BoundingBox _boundingBox;
    private final List<Line> _sides;

    private Polygon(List<Line> sides, BoundingBox boundingBox) {
        _sides = sides;
        _boundingBox = boundingBox;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Point> _vertexes = new ArrayList<Point>();
        private List<Line> _sides = new ArrayList<Line>();
        private BoundingBox _boundingBox = null;

        private boolean _firstPoint = true;
        private boolean _isClosed = false;

        public Builder addVertex(Point point) {
            if (_isClosed) {
                // each hole we start with the new array of vertex points
                _vertexes = new ArrayList<Point>();
                _isClosed = false;
            }

            updateBoundingBox(point);
            _vertexes.add(point);

            // add line (edge) to the polygon
            if (_vertexes.size() > 1) {
                Line Line = new Line(_vertexes.get(_vertexes.size() - 2), point);
                _sides.add(Line);
            }

            return this;
        }

        public Builder close() {
            validate();

            // add last Line
            _sides.add(new Line(_vertexes.get(_vertexes.size() - 1), _vertexes.get(0)));
            _isClosed = true;

            return this;
        }

        public Polygon build() {
            validate();

            // in case you forgot to close
            if (!_isClosed) {
                // add last Line
                _sides.add(new Line(_vertexes.get(_vertexes.size() - 1), _vertexes.get(0)));
            }

            Polygon polygon = new Polygon(_sides, _boundingBox);
            return polygon;
        }

        private void updateBoundingBox(Point point) {
            if (_firstPoint) {
                _boundingBox = new BoundingBox();
                _boundingBox.xMax = point.x;
                _boundingBox.xMin = point.x;
                _boundingBox.yMax = point.y;
                _boundingBox.yMin = point.y;

                _firstPoint = false;
            } else {
                // set bounding box
                if (point.x > _boundingBox.xMax) {
                    _boundingBox.xMax = point.x;
                } else if (point.x < _boundingBox.xMin) {
                    _boundingBox.xMin = point.x;
                }
                if (point.y > _boundingBox.yMax) {
                    _boundingBox.yMax = point.y;
                } else if (point.y < _boundingBox.yMin) {
                    _boundingBox.yMin = point.y;
                }
            }
        }

        private void validate() {
            if (_vertexes.size() < 3) {
                throw new RuntimeException("Polygon must have at least 3 points");
            }
        }
    }

    public boolean contains(Point point) {
        if (inBoundingBox(point)) {
            Line ray = createRay(point);
            int intersection = 0;
            for (Line side : _sides) {
                if (intersect(ray, side)) {
                    // System.out.println("intersection++");
                    intersection++;
                }
            }

            /*
             * If the number of intersections is odd, then the point is inside the polygon
             */
            if (intersection % 2 == 1) {
                return true;
            }
        }
        return false;
    }

    public List<Line> getSides() {
        return _sides;
    }

    private boolean intersect(Line ray, Line side) {
        Point intersectPoint = null;

        // if both vectors aren't from the kind of x=1 lines then go into
        if (!ray.isVertical() && !side.isVertical()) {
            // check if both vectors are parallel. If they are parallel then no intersection point will exist
            if (ray.getA() - side.getA() == 0) {
                return false;
            }

            float x = ((side.getB() - ray.getB()) / (ray.getA() - side.getA())); // x = (b2-b1)/(a1-a2)
            float y = side.getA() * x + side.getB(); // y = a2*x+b2
            intersectPoint = new Point(x, y);
        } else if (ray.isVertical() && !side.isVertical()) {
            float x = ray.getStart().x;
            float y = side.getA() * x + side.getB();
            intersectPoint = new Point(x, y);
        } else if (!ray.isVertical() && side.isVertical()) {
            float x = side.getStart().x;
            float y = ray.getA() * x + ray.getB();
            intersectPoint = new Point(x, y);
        } else {
            return false;
        }

        // System.out.println("Ray: " + ray.toString() + " ,Side: " + side);
        // System.out.println("Intersect point: " + intersectPoint.toString());

        if (side.isInside(intersectPoint) && ray.isInside(intersectPoint)) {
            return true;
        }

        return false;
    }

    private Line createRay(Point point) {
        // create outside point
        float epsilon = (_boundingBox.xMax - _boundingBox.xMin) / 100f;
        Point outsidePoint = new Point(_boundingBox.xMin - epsilon, _boundingBox.yMin);

        Line vector = new Line(outsidePoint, point);
        return vector;
    }

    private boolean inBoundingBox(Point point) {
        if (point.x < _boundingBox.xMin || point.x > _boundingBox.xMax || point.y < _boundingBox.yMin || point.y > _boundingBox.yMax) {
            return false;
        }
        return true;
    }

    private static class BoundingBox {
        public float xMax = Float.NEGATIVE_INFINITY;
        public float xMin = Float.NEGATIVE_INFINITY;
        public float yMax = Float.NEGATIVE_INFINITY;
        public float yMin = Float.NEGATIVE_INFINITY;
    }

    public static void initialize() {
        Polygon klojen;
        Polygon blimbing;
        Polygon kedungkandang;
        Polygon lowokwaru;
        Polygon sukun;

        klojen = Polygon.Builder()
                .addVertex(new Point(-7.951485f, 112.615699f))
                .addVertex(new Point(-7.952633f, 112.616943f))
                .addVertex(new Point(-7.954971f, 112.616257f))
                .addVertex(new Point(-7.956076f, 112.615914f))
                .addVertex(new Point(-7.959943f, 112.621578f))
                .addVertex(new Point(-7.961898f, 112.621106f))
                .addVertex(new Point(-7.963556f, 112.621707f))
                .addVertex(new Point(-7.966786f, 112.619304f))
                .addVertex(new Point(-7.964066f, 112.613038f))
                .addVertex(new Point(-7.971504f, 112.612523f))
                .addVertex(new Point(-7.971971f, 112.613124f))
                .addVertex(new Point(-7.977505f, 112.612368f))
                .addVertex(new Point(-7.981397f, 112.614511f))
                .addVertex(new Point(-7.983368f, 112.614103f))
                .addVertex(new Point(-7.983419f, 112.623085f))
                .addVertex(new Point(-7.985390f, 112.621962f))
                .addVertex(new Point(-7.987917f, 112.623136f))
                .addVertex(new Point(-7.993072f, 112.623340f))
                .addVertex(new Point(-7.996306f, 112.624106f))
                .addVertex(new Point(-7.998732f, 112.623289f))
                .addVertex(new Point(-7.998631f, 112.625076f))
                .addVertex(new Point(-7.996205f, 112.625535f))
                .addVertex(new Point(-7.991101f, 112.628495f))
                .addVertex(new Point(-7.990841f, 112.628788f))
                .addVertex(new Point(-7.992031f, 112.630676f))
                .addVertex(new Point(-7.993306f, 112.633080f))
                .addVertex(new Point(-7.978516f, 112.638058f))
                .addVertex(new Point(-7.966956f, 112.637886f))
                .addVertex(new Point(-7.965086f, 112.639174f))
                .addVertex(new Point(-7.964151f, 112.637886f))
                .addVertex(new Point(-7.962536f, 112.639431f))
                .addVertex(new Point(-7.960751f, 112.636770f))
                .addVertex(new Point(-7.964491f, 112.634539f))
                .addVertex(new Point(-7.962026f, 112.630076f))
                .addVertex(new Point(-7.960921f, 112.631020f))
                .addVertex(new Point(-7.960921f, 112.631020f))
                .addVertex(new Point(-7.950550f, 112.616600f))
                .addVertex(new Point(-7.951485f, 112.615699f))
                .build();

        blimbing = Polygon.Builder()
                .addVertex(new Point(-7.987069f, 112.635555f))
                .addVertex(new Point(-7.988769f, 112.638816f))
                .addVertex(new Point(-7.993359f, 112.645168f))
                .addVertex(new Point(-7.987239f, 112.649288f))
                .addVertex(new Point(-7.974659f, 112.650661f))
                .addVertex(new Point(-7.970239f, 112.654266f))
                .addVertex(new Point(-7.963609f, 112.657527f))
                .addVertex(new Point(-7.961526f, 112.659029f))
                .addVertex(new Point(-7.956468f, 112.663364f))
                .addVertex(new Point(-7.948308f, 112.663364f))
                .addVertex(new Point(-7.945928f, 112.666110f))
                .addVertex(new Point(-7.945928f, 112.666110f))
                .addVertex(new Point(-7.946098f, 112.667999f))
                .addVertex(new Point(-7.943547f, 112.665252f))
                .addVertex(new Point(-7.939977f, 112.663879f))
                .addVertex(new Point(-7.936407f, 112.664394f))
                .addVertex(new Point(-7.927226f, 112.662162f))
                .addVertex(new Point(-7.919405f, 112.649459f))
                .addVertex(new Point(-7.918207f, 112.649690f))
                .addVertex(new Point(-7.916491f, 112.645648f))
                .addVertex(new Point(-7.914819f, 112.645861f))
                .addVertex(new Point(-7.914940f, 112.644465f))
                .addVertex(new Point(-7.918126f, 112.642948f))
                .addVertex(new Point(-7.919148f, 112.643251f))
                .addVertex(new Point(-7.919749f, 112.642705f))
                .addVertex(new Point(-7.919448f, 112.641856f))
                .addVertex(new Point(-7.920590f, 112.640824f))
                .addVertex(new Point(-7.923055f, 112.639124f))
                .addVertex(new Point(-7.930093f, 112.637566f))
                .addVertex(new Point(-7.930972f, 112.639045f))
                .addVertex(new Point(-7.931694f, 112.639728f))
                .addVertex(new Point(-7.931341f, 112.640305f))
                .addVertex(new Point(-7.931619f, 112.640897f))
                .addVertex(new Point(-7.931694f, 112.641299f))
                .addVertex(new Point(-7.932866f, 112.641875f))
                .addVertex(new Point(-7.933775f, 112.642695f))
                .addVertex(new Point(-7.935586f, 112.641845f))
                .addVertex(new Point(-7.935669f, 112.642057f))
                .addVertex(new Point(-7.936149f, 112.641898f))
                .addVertex(new Point(-7.936074f, 112.641708f))
                .addVertex(new Point(-7.937074f, 112.641276f))
                .addVertex(new Point(-7.938171f, 112.641230f))
                .addVertex(new Point(-7.938809f, 112.640768f))
                .addVertex(new Point(-7.938621f, 112.640548f))
                .addVertex(new Point(-7.939320f, 112.640237f))
                .addVertex(new Point(-7.944958f, 112.636820f))
                .addVertex(new Point(-7.946301f, 112.638364f))
                .addVertex(new Point(-7.952719f, 112.638042f))
                .addVertex(new Point(-7.953016f, 112.638793f))
                .addVertex(new Point(-7.960591f, 112.636928f))
                .addVertex(new Point(-7.962546f, 112.639546f))
                .addVertex(new Point(-7.964119f, 112.638430f))
                .addVertex(new Point(-7.964969f, 112.639503f))
                .addVertex(new Point(-7.967136f, 112.638387f))
                .addVertex(new Point(-7.977974f, 112.638473f))
                .addVertex(new Point(-7.987069f, 112.635598f))
                .addVertex(new Point(-7.987069f, 112.635555f))
                .build();

        kedungkandang = Polygon.Builder()
                .addVertex(new Point(-7.987753f, 112.635724f))
                .addVertex(new Point(-8.003500f, 112.630687f))
                .addVertex(new Point(-8.004221f, 112.631355f))
                .addVertex(new Point(-8.005964f, 112.631537f))
                .addVertex(new Point(-8.010892f, 112.632144f))
                .addVertex(new Point(-8.017142f, 112.631597f))
                .addVertex(new Point(-8.020448f, 112.633661f))
                .addVertex(new Point(-8.026231f, 112.631251f))
                .addVertex(new Point(-8.029776f, 112.629309f))
                .addVertex(new Point(-8.033743f, 112.629430f))
                .addVertex(new Point(-8.034885f, 112.631129f))
                .addVertex(new Point(-8.045521f, 112.631918f))
                .addVertex(new Point(-8.046182f, 112.634953f))
                .addVertex(new Point(-8.049427f, 112.642357f))
                .addVertex(new Point(-8.048045f, 112.645271f))
                .addVertex(new Point(-8.050870f, 112.645574f))
                .addVertex(new Point(-8.047745f, 112.648609f))
                .addVertex(new Point(-8.049968f, 112.656498f))
                .addVertex(new Point(-8.044981f, 112.657409f))
                .addVertex(new Point(-8.039572f, 112.662264f))
                .addVertex(new Point(-8.040594f, 112.665177f))
                .addVertex(new Point(-8.034825f, 112.667666f))
                .addVertex(new Point(-8.031820f, 112.672642f))
                .addVertex(new Point(-8.027673f, 112.675070f))
                .addVertex(new Point(-8.025029f, 112.671914f))
                .addVertex(new Point(-8.018538f, 112.672885f))
                .addVertex(new Point(-8.008922f, 112.671793f))
                .addVertex(new Point(-8.010004f, 112.678529f))
                .addVertex(new Point(-8.006699f, 112.678833f))
                .addVertex(new Point(-8.004956f, 112.679076f))
                .addVertex(new Point(-8.004655f, 112.684234f))
                .addVertex(new Point(-7.988909f, 112.692671f))
                .addVertex(new Point(-7.982658f, 112.694552f))
                .addVertex(new Point(-7.980735f, 112.688665f))
                .addVertex(new Point(-7.975626f, 112.689636f))
                .addVertex(new Point(-7.976047f, 112.685812f))
                .addVertex(new Point(-7.975446f, 112.681625f))
                .addVertex(new Point(-7.977189f, 112.680532f))
                .addVertex(new Point(-7.974845f, 112.675434f))
                .addVertex(new Point(-7.972921f, 112.675798f))
                .addVertex(new Point(-7.969736f, 112.668273f))
                .addVertex(new Point(-7.971659f, 112.663417f))
                .addVertex(new Point(-7.970517f, 112.662203f))
                .addVertex(new Point(-7.968534f, 112.663053f))
                .addVertex(new Point(-7.965588f, 112.657166f))
                .addVertex(new Point(-7.970637f, 112.654920f))
                .addVertex(new Point(-7.974845f, 112.651036f))
                .addVertex(new Point(-7.987046f, 112.649883f))
                .addVertex(new Point(-7.993537f, 112.645756f))
                .addVertex(new Point(-7.987753f, 112.635724f))
                .build();

        lowokwaru = Polygon.Builder()
                .addVertex(new Point(-7.938586f, 112.569148f))
                .addVertex(new Point(-7.943092f, 112.579791f))
                .addVertex(new Point(-7.947682f, 112.586400f))
                .addVertex(new Point(-7.946832f, 112.587516f))
                .addVertex(new Point(-7.947937f, 112.588975f))
                .addVertex(new Point(-7.946747f, 112.589661f))
                .addVertex(new Point(-7.950232f, 112.598330f))
                .addVertex(new Point(-7.949807f, 112.600476f))
                .addVertex(new Point(-7.950912f, 112.601334f))
                .addVertex(new Point(-7.950147f, 112.603480f))
                .addVertex(new Point(-7.953325f, 112.602941f))
                .addVertex(new Point(-7.953916f, 112.603256f))
                .addVertex(new Point(-7.954504f, 112.604165f))
                .addVertex(new Point(-7.954272f, 112.607954f))
                .addVertex(new Point(-7.958061f, 112.606240f))
                .addVertex(new Point(-7.958364f, 112.606367f))
                .addVertex(new Point(-7.958507f, 112.607539f))
                .addVertex(new Point(-7.959562f, 112.607485f))
                .addVertex(new Point(-7.960259f, 112.607792f))
                .addVertex(new Point(-7.961384f, 112.608568f))
                .addVertex(new Point(-7.961903f, 112.608748f))
                .addVertex(new Point(-7.962140f, 112.611496f))
                .addVertex(new Point(-7.962671f, 112.612389f))
                .addVertex(new Point(-7.964010f, 112.612798f))
                .addVertex(new Point(-7.963985f, 112.614099f))
                .addVertex(new Point(-7.966714f, 112.619202f))
                .addVertex(new Point(-7.963656f, 112.621627f))
                .addVertex(new Point(-7.961812f, 112.620989f))
                .addVertex(new Point(-7.959914f, 112.621410f))
                .addVertex(new Point(-7.956101f, 112.615651f))
                .addVertex(new Point(-7.955165f, 112.616295f))
                .addVertex(new Point(-7.955017f, 112.616102f))
                .addVertex(new Point(-7.952679f, 112.616745f))
                .addVertex(new Point(-7.951574f, 112.615565f))
                .addVertex(new Point(-7.950299f, 112.616574f))
                .addVertex(new Point(-7.956101f, 112.623547f))
                .addVertex(new Point(-7.956101f, 112.624384f))
                .addVertex(new Point(-7.957567f, 112.625650f))
                .addVertex(new Point(-7.960776f, 112.631272f))
                .addVertex(new Point(-7.962051f, 112.630307f))
                .addVertex(new Point(-7.964558f, 112.634727f))
                .addVertex(new Point(-7.953083f, 112.638654f))
                .addVertex(new Point(-7.952707f, 112.637788f))
                .addVertex(new Point(-7.945002f, 112.636739f))
                .addVertex(new Point(-7.937127f, 112.641473f))
                .addVertex(new Point(-7.933881f, 112.642626f))
                .addVertex(new Point(-7.931687f, 112.641322f))
                .addVertex(new Point(-7.931357f, 112.640320f))
                .addVertex(new Point(-7.931718f, 112.639774f))
                .addVertex(new Point(-7.930816f, 112.638712f))
                .addVertex(new Point(-7.928291f, 112.639410f))
                .addVertex(new Point(-7.925857f, 112.636921f))
                .addVertex(new Point(-7.923272f, 112.638924f))
                .addVertex(new Point(-7.920507f, 112.639926f))
                .addVertex(new Point(-7.912391f, 112.631490f))
                .addVertex(new Point(-7.911971f, 112.630367f))
                .addVertex(new Point(-7.911610f, 112.630488f))
                .addVertex(new Point(-7.911369f, 112.630094f))
                .addVertex(new Point(-7.911550f, 112.627696f))
                .addVertex(new Point(-7.913684f, 112.625815f))
                .addVertex(new Point(-7.913924f, 112.624510f))
                .addVertex(new Point(-7.918283f, 112.620929f))
                .addVertex(new Point(-7.918222f, 112.618714f))
                .addVertex(new Point(-7.919875f, 112.617773f))
                .addVertex(new Point(-7.919605f, 112.616681f))
                .addVertex(new Point(-7.920507f, 112.615133f))
                .addVertex(new Point(-7.919815f, 112.613161f))
                .addVertex(new Point(-7.919785f, 112.610733f))
                .addVertex(new Point(-7.922881f, 112.611188f))
                .addVertex(new Point(-7.923924f, 112.609493f))
                .addVertex(new Point(-7.921760f, 112.602514f))
                .addVertex(new Point(-7.924104f, 112.600298f))
                .addVertex(new Point(-7.923503f, 112.599479f))
                .addVertex(new Point(-7.922631f, 112.600056f))
                .addVertex(new Point(-7.921820f, 112.598872f))
                .addVertex(new Point(-7.922030f, 112.597840f))
                .addVertex(new Point(-7.922962f, 112.597567f))
                .addVertex(new Point(-7.920497f, 112.596293f))
                .addVertex(new Point(-7.920077f, 112.594897f))
                .addVertex(new Point(-7.918965f, 112.594442f))
                .addVertex(new Point(-7.918514f, 112.593380f))
                .addVertex(new Point(-7.917582f, 112.592803f))
                .addVertex(new Point(-7.917762f, 112.591528f))
                .addVertex(new Point(-7.918814f, 112.593380f))
                .addVertex(new Point(-7.920047f, 112.594138f))
                .addVertex(new Point(-7.923142f, 112.593349f))
                .addVertex(new Point(-7.923189f, 112.595331f))
                .addVertex(new Point(-7.922467f, 112.595588f))
                .addVertex(new Point(-7.923295f, 112.596575f))
                .addVertex(new Point(-7.926356f, 112.600137f))
                .addVertex(new Point(-7.930266f, 112.599236f))
                .addVertex(new Point(-7.930011f, 112.597519f))
                .addVertex(new Point(-7.931371f, 112.597476f))
                .addVertex(new Point(-7.932540f, 112.598313f))
                .addVertex(new Point(-7.933413f, 112.597970f))
                .addVertex(new Point(-7.934986f, 112.598185f))
                .addVertex(new Point(-7.935474f, 112.599129f))
                .addVertex(new Point(-7.937430f, 112.597498f))
                .addVertex(new Point(-7.936898f, 112.596361f))
                .addVertex(new Point(-7.937663f, 112.596168f))
                .addVertex(new Point(-7.936643f, 112.585289f))
                .addVertex(new Point(-7.937918f, 112.585267f))
                .addVertex(new Point(-7.937302f, 112.580075f))
                .addVertex(new Point(-7.936856f, 112.580096f))
                .addVertex(new Point(-7.936579f, 112.578852f))
                .addVertex(new Point(-7.936898f, 112.578401f))
                .addVertex(new Point(-7.935301f, 112.573906f))
                .addVertex(new Point(-7.935875f, 112.570773f))
                .addVertex(new Point(-7.936513f, 112.570795f))
                .addVertex(new Point(-7.936768f, 112.569636f))
                .addVertex(new Point(-7.938586f, 112.569148f))
                .build();

        sukun = Polygon.Builder()
                .addVertex(new Point(-8.032976f, 112.628842f))
                .addVertex(new Point(-8.026905f, 112.630063f))
                .addVertex(new Point(-8.020467f, 112.633089f))
                .addVertex(new Point(-8.017172f, 112.631120f))
                .addVertex(new Point(-8.013751f, 112.631399f))
                .addVertex(new Point(-8.004614f, 112.630669f))
                .addVertex(new Point(-8.004210f, 112.631077f))
                .addVertex(new Point(-8.003764f, 112.630133f))
                .addVertex(new Point(-7.993636f, 112.633179f))
                .addVertex(new Point(-7.992653f, 112.630670f))
                .addVertex(new Point(-7.992028f, 112.630761f))
                .addVertex(new Point(-7.991277f, 112.628650f))
                .addVertex(new Point(-7.996584f, 112.626322f))
                .addVertex(new Point(-7.996387f, 112.625690f))
                .addVertex(new Point(-7.999086f, 112.625203f))
                .addVertex(new Point(-7.998978f, 112.622894f))
                .addVertex(new Point(-7.998139f, 112.622749f))
                .addVertex(new Point(-7.997978f, 112.623381f))
                .addVertex(new Point(-7.996352f, 112.623850f))
                .addVertex(new Point(-7.988525f, 112.621919f))
                .addVertex(new Point(-7.987757f, 112.622930f))
                .addVertex(new Point(-7.985095f, 112.621811f))
                .addVertex(new Point(-7.983504f, 112.622912f))
                .addVertex(new Point(-7.983719f, 112.613980f))
                .addVertex(new Point(-7.981449f, 112.614341f))
                .addVertex(new Point(-7.977196f, 112.612158f))
                .addVertex(new Point(-7.972050f, 112.613006f))
                .addVertex(new Point(-7.971675f, 112.612284f))
                .addVertex(new Point(-7.965492f, 112.613186f))
                .addVertex(new Point(-7.962669f, 112.612338f))
                .addVertex(new Point(-7.961954f, 112.608765f))
                .addVertex(new Point(-7.960313f, 112.607719f))
                .addVertex(new Point(-7.959618f, 112.607706f))
                .addVertex(new Point(-7.959593f, 112.607463f))
                .addVertex(new Point(-7.958544f, 112.607540f))
                .addVertex(new Point(-7.958229f, 112.606136f))
                .addVertex(new Point(-7.954324f, 112.607782f))
                .addVertex(new Point(-7.954564f, 112.604146f))
                .addVertex(new Point(-7.953402f, 112.602870f))
                .addVertex(new Point(-7.950188f, 112.603456f))
                .addVertex(new Point(-7.951072f, 112.601134f))
                .addVertex(new Point(-7.949859f, 112.600432f))
                .addVertex(new Point(-7.950327f, 112.598493f))
                .addVertex(new Point(-7.948065f, 112.592586f))
                .addVertex(new Point(-7.950074f, 112.591807f))
                .addVertex(new Point(-7.949745f, 112.590672f))
                .addVertex(new Point(-7.950112f, 112.590123f))
                .addVertex(new Point(-7.954358f, 112.589855f))
                .addVertex(new Point(-7.955482f, 112.591042f))
                .addVertex(new Point(-7.954623f, 112.592165f))
                .addVertex(new Point(-7.954787f, 112.595890f))
                .addVertex(new Point(-7.956733f, 112.599322f))
                .addVertex(new Point(-7.959690f, 112.599424f))
                .addVertex(new Point(-7.962862f, 112.598136f))
                .addVertex(new Point(-7.965111f, 112.600483f))
                .addVertex(new Point(-7.967145f, 112.602295f))
                .addVertex(new Point(-7.968876f, 112.602805f))
                .addVertex(new Point(-7.971744f, 112.602946f))
                .addVertex(new Point(-7.972679f, 112.600738f))
                .addVertex(new Point(-7.972161f, 112.599807f))
                .addVertex(new Point(-7.974726f, 112.598850f))
                .addVertex(new Point(-7.973614f, 112.595609f))
                .addVertex(new Point(-7.973891f, 112.593775f))
                .addVertex(new Point(-7.973246f, 112.591070f))
                .addVertex(new Point(-7.975154f, 112.589552f))
                .addVertex(new Point(-7.979589f, 112.598496f))
                .addVertex(new Point(-7.981169f, 112.597832f))
                .addVertex(new Point(-7.981800f, 112.599389f))
                .addVertex(new Point(-7.984631f, 112.597960f))
                .addVertex(new Point(-7.984896f, 112.592091f))
                .addVertex(new Point(-7.982659f, 112.590355f))
                .addVertex(new Point(-7.983064f, 112.587796f))
                .addVertex(new Point(-7.982812f, 112.585499f))
                .addVertex(new Point(-7.981902f, 112.585423f))
                .addVertex(new Point(-7.980158f, 112.581876f))
                .addVertex(new Point(-7.984757f, 112.578355f))
                .addVertex(new Point(-7.985389f, 112.580166f))
                .addVertex(new Point(-7.986678f, 112.586112f))
                .addVertex(new Point(-7.985162f, 112.588408f))
                .addVertex(new Point(-7.986097f, 112.591394f))
                .addVertex(new Point(-7.992364f, 112.589148f))
                .addVertex(new Point(-7.995851f, 112.598386f))
                .addVertex(new Point(-7.991429f, 112.599942f))
                .addVertex(new Point(-7.992364f, 112.601525f))
                .addVertex(new Point(-8.005984f, 112.597187f))
                .addVertex(new Point(-8.006338f, 112.601014f))
                .addVertex(new Point(-8.012705f, 112.607343f))
                .addVertex(new Point(-8.018921f, 112.608261f))
                .addVertex(new Point(-8.016672f, 112.609180f))
                .addVertex(new Point(-8.015055f, 112.613288f))
                .addVertex(new Point(-8.009673f, 112.61609f))
                .addVertex(new Point(-8.011316f, 112.618672f))
                .addVertex(new Point(-8.015611f, 112.617448f))
                .addVertex(new Point(-8.016824f, 112.617907f))
                .addVertex(new Point(-8.019300f, 112.615406f))
                .addVertex(new Point(-8.019679f, 112.613645f))
                .addVertex(new Point(-8.020614f, 112.613569f))
                .addVertex(new Point(-8.023343f, 112.619004f))
                .addVertex(new Point(-8.026426f, 112.617830f))
                .addVertex(new Point(-8.027411f, 112.620943f))
                .addVertex(new Point(-8.028447f, 112.622168f))
                .addVertex(new Point(-8.029382f, 112.622015f))
                .addVertex(new Point(-8.033122f, 112.628879f))
                .addVertex(new Point(-8.032976f, 112.628842f))
                .build();

        regionList.add(new Region("Klojen", klojen));
        regionList.add(new Region("Blimbing", blimbing));
        regionList.add(new Region("Kedung Kandang", kedungkandang));
        regionList.add(new Region("Lowokwaru", lowokwaru));
        regionList.add(new Region("Sukun", sukun));
    }
}
