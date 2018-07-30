import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;




 public class TaskRunner {
        public TaskRunner() {

        }

        public static void main(String [] args)
        {
            long startT = System.currentTimeMillis();
            int thred_count = 1;
            int width = 800;
            int height= 600;
            boolean flag = false;
            double maxRe = 2.0;
            double minRe = -2.0;
            double maxIm = 1.3;
            double minIm = -1.3;

            String str = "zad16";


            try{
                Options options = new Options();


                options.addOption("s",true, " set resolution");
                options.addOption("t",true, " set maximum number of threads");
                options.addOption("o",true, " set name of image");
                options.addOption("q"," quiet mode on ");
                options.addOption("r",true," insert range ");


                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = null;


                cmd = parser.parse(options, args);

                if (cmd.hasOption("s"))
                {
                    String res = cmd.getOptionValue("s");
                    String [] hw = res.split("x");

                    width = Integer.parseInt(hw[0]);
                    height = Integer.parseInt(hw[1]);
                }


                if(cmd.hasOption("t"))
                {
                    thred_count = Integer.parseInt(cmd.getOptionValue("t"));
                }


                if(cmd.hasOption("o"))
                {
                    str = cmd.getOptionValue("o");
                }

                if(cmd.hasOption("q"))
                {
                    flag = true;
                }

                if(cmd.hasOption("r"))
                {
                    String r = cmd.getOptionValue("r");
                    String [] arr = r.split(":");

                    minRe = Double.parseDouble(arr[0]);
                    maxRe = Double.parseDouble(arr[1]);
                    minIm = Double.parseDouble(arr[2]);
                    maxIm = Double.parseDouble(arr[3]);

                }

            }


            catch(ParseException e)
            {
                System.out.println(e.getMessage());
            }

            Thread[] tr = new Thread[thred_count];
            int chunk_size = height/thred_count;
            int rem = height % thred_count;



            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


            for(int i = 0; i < thred_count; ++i)
            {
                runable r = null;

                r = new runable(thred_count, chunk_size,i,bi,rem,flag,minRe,maxRe,minIm,maxIm);

                Thread t = new Thread(r);
                tr[i] = t;
                t.start();
            }


            for(int i = 0; i < thred_count; i++)
            {
                try{
                    tr[i].join();
                }
                catch(InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }

            try{
                ImageIO.write(bi, "PNG", new File(str));
            }

            catch(IOException f){
                System.out.println(f.getMessage());
            }

            long endT = System.currentTimeMillis();
            long duration = (endT - startT);
            System.out.println("Total execution run "+duration);
        }


    }


