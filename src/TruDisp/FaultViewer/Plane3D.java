package TruDisp.FaultViewer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Nieto on 18/08/15.
 */
public class Plane3D extends My3DObject {


    private My3DObject obj;

    public Plane3D ()
    {

        ArrayList<Double[]> nv = new ArrayList<>();
        ArrayList<Color> color = new ArrayList<>();

        Double[] vector = new Double[]{1.0,1.0,1.0};
        Double vectornorm=normOfVector(vector);
        vector[X]/=vectornorm;vector[Y]/=vectornorm;vector[Z]/=vectornorm;
        nv.add(vector);

        color.add(Color.rgb(255, 0, 0, .5));

        setObjFaceColor(color);
        setNormalVect(nv);
    }

    public Plane3D(Double[] normalVector, Color color,My3DObject container)
    {
        super();

        this.getObjNormalVect().add(normalVector);
        this.getObjFaceColor().add(color);
        this.setContainer(container);
        this.setRotMatrix(container.getRotMatrix());
        this.setObjPos(new Double[]{0d,0d,0d});
        this.intersectPlaneWidthContainer();



    }

    public void setContainer(My3DObject object)
    {
        obj= object;
    }

    public void intersectPlaneWidthContainer()
    {
        Double[] nv = this.getObjNormalVect().get(0);       // obtenemos el arreglo de vectores normales a los planos de las caras.
        ArrayList<Double[]> vert = new ArrayList<>();       // Vertices del plano que sera´ calculados.
        ArrayList<int[]> face = new ArrayList<>();          // Vector que contendrá el orden de los vertices.

        Double[] planeOrigin= this.getObjPos();
        Double[] l = new Double[3];     // Arista;
        Double[] linePoint;             // Punto de la linea.
        Double[] val;                   // Punto de intersección.
        Double[] po_pl= new Double[3];  // Diferencia entre el punto del plano y el de la recta.
        Double a,num,den;

        for (int i=0;i<obj.getObjFace().size();i++) // Para cada cara del contenedor.
        {
            for(int j=0;j<obj.getObjFace().get(i).length;j++) // Para cada vertice del contenedor
            {
                // Obtenemos la arista
                if(j==obj.getObjFace().get(i).length-1)
                {
                    l[X]=obj.getTObjVert().get(obj.getObjFace().get(i)[0])[X]-obj.getTObjVert().get(obj.getObjFace().get(i)[j])[X];
                    l[Y]=obj.getTObjVert().get(obj.getObjFace().get(i)[0])[Y]-obj.getTObjVert().get(obj.getObjFace().get(i)[j])[Y];
                    l[Z]=obj.getTObjVert().get(obj.getObjFace().get(i)[0])[Z]-obj.getTObjVert().get(obj.getObjFace().get(i)[j])[Z];
                }
                else
                {
                    l[X] = obj.getTObjVert().get(obj.getObjFace().get(i)[j + 1])[X] - obj.getTObjVert().get(obj.getObjFace().get(i)[j])[X];
                    l[Y] = obj.getTObjVert().get(obj.getObjFace().get(i)[j + 1])[Y] - obj.getTObjVert().get(obj.getObjFace().get(i)[j])[Y];
                    l[Z] = obj.getTObjVert().get(obj.getObjFace().get(i)[j + 1])[Z] - obj.getTObjVert().get(obj.getObjFace().get(i)[j])[Z];
                }

                // obtenemos un punto de la arista.
                linePoint = obj.getTObjVert().get(obj.getObjFace().get(i)[j]);

                // punto del plano - punto de la arista.
                po_pl[X]= planeOrigin[X]-linePoint[X]; po_pl[Y]=planeOrigin[Y]-linePoint[Y]; po_pl[Z]=planeOrigin[Z]-linePoint[Z];

                num = dotProduct(po_pl,nv);
                den = dotProduct(l,nv);

                if(den!=0 && num !=0)
                {
                    a = num/den;    // Obtenemos el escalar que nos da la intersección de la arista con el plano.
                    val = new Double[]{linePoint[X]+(a*l[X]),linePoint[Y]+(a*l[Y]),linePoint[Z]+(a*l[Z])}; // Obtenemos el punto de intersección.

                    boolean addval = true;
                    for (int n =0;n<vert.size();n++)
                    {
                        // revisamos si no esta repetido el vertice.
                        if(normOfVector(new Double[]{vert.get(n)[X] - val[X], vert.get(n)[Y] - val[Y], vert.get(n)[Z] - val[Z]})==0)
                        {
                            addval=false;
                            break;
                        }
                    }

                    //!!! Importante, al calcularse las itersecciones con las caras se encuentran las intersecciones fuera del objeto, hay que arreglar eso
                    if(normOfVector(val)>Math.sqrt(3)+.1)
                        addval =false;

                    // Agregamos el nuevo vertice del plano.
                    if (addval) { vert.add(val);}
                }
            }
        }
        face.add(orderFaceVertices(vert, nv));
        setObject(vert, face, this.getObjFaceColor());
        project();
    }

    public void setObjFaceColor(Color color)
    {
        getObjFaceColor().set(0,color);

    }

    public void setNormalVector(Double[] nv)
    {
        this.getObjNormalVect().set(0,nv);
    }



}
