
import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.math3.complex.Complex;
import java.awt.image.BufferedImage;

public class runable implements Runnable{

    int thred_count;
    int chunk_size;
    int current;
    int rem;
    boolean flag;
    BufferedImage bi;
    double maxRe;
    double minRe;
    double maxIm;
    double minIm;


    public runable (int _thred_count, int _chunk_size, int _current, BufferedImage _bi,int _rem, boolean _flag, double _minRe, double _maxRe, double _minIm, double _maxIm)
    {
        this.thred_count = _thred_count;
        this.chunk_size = _chunk_size;
        this. rem = _rem;
        this.current = _current;
        this.flag = _flag;
        this.bi = _bi;
        this.maxRe = _maxRe;
        this.minRe = _minRe;
        this.minIm = _minIm;
        this.maxIm = _maxIm;
    }

    private static Complex z_iter(Complex z, Complex c)
    {
        Complex i = new Complex(0.0,1.0);
        return c.multiply(Math.exp(-z.getReal())).multiply(i.multiply(Math.sin(-z.getImaginary())).add(Math.cos(-z.getImaginary()))).add(z.multiply(z));

    }

    private static int z_check(Complex c){

        Complex z0 = new Complex(0.0,0.0);
        Complex z_prev = z0;
        Complex zi = null;
        int steps = 0;
        Double d = null;

        for(steps = 0; steps < 1000; steps++)
        {
            zi = z_iter(z_prev,c);
            z_prev = zi;


            d = new Double(z_prev.getReal());

            if(d.isInfinite() || d.isNaN())
            {
                break;
            }
        }

        return steps;
    }

    public void run()
    {
        long startTime = System.currentTimeMillis();


        if(!flag) {
            System.out.println("Thread " + (current + 1) + " started");
        }

        int startRow = current *chunk_size;
        int endRow = (current + 1) * chunk_size;

        if(current == thred_count - 1) {
            endRow += rem;
        }

        int width = bi.getWidth();
        int height = bi.getHeight();

        double reFactor = (maxRe - minRe) / (width - 1);
        double imFactor = (maxIm - minIm)/(height -1);


        for(int i = startRow; i < endRow; ++i) {
            double im = maxIm - i*imFactor;

            for(int j = 0; j < width; ++j) {
                double re = minRe + j*reFactor;

                int steps = z_check(new Complex(re, im));


                if(steps == 1000) {
                    bi.setRGB(j, i, 0x000000);
                }

                else {
                    bi.setRGB(j, i, 0xFFFFFF);
                }
            }
        }

        if(!flag) {
            long endTime = System.currentTimeMillis();

            long duration = (endTime - startTime);
            System.out.println("Thread " + (current + 1) + " ended " + "excecution  time was " + duration);
        }
    }
}
