
package flash.cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 *
 * @author prashish
 */

public class FlashCore {
    
    File file;
    RandomAccessFile reader;                 // for buffering
    RandomAccessFile writer;
    String sourceFilename;

    FlashCore( String sourcefilename )
    {
        sourceFilename = sourcefilename;
        file = new File(sourceFilename);
        reader = null;
        writer = null;
    }
    
        // opening file to read - reader pointer
    boolean openFiletoRead()
    {
         try {

         reader = new RandomAccessFile(file, "rw");

         }
         catch ( FileNotFoundException e)
         {
             System.out.println("File Not Found");
             return false;
         }
         finally {
             return true;
         }
    }

    // opening file to write - writer pointer
    boolean openFiletoWrite()
    {
         try {
            writer = new RandomAccessFile(file, "rw");
         }
         catch ( IOException e)
         {
             System.out.println("IO error");
             return false;
         }
         finally {
             return true;
         }
    }

    // close reader
    void closeReadFile()
    {
        try {
            reader.close();
        }
        catch ( IOException e )
        {
             System.out.println("IO error");
        }
    }

    // close writer
    boolean closeWriteFile()
    {
        try {
            writer.close();
        }
        catch ( IOException e )
        {
             System.out.println("IO error");
             return false;
        }
        finally {
             return true;
        }
    }
    
        // start reading from reader pointer
    String readFromfile() {
        String line;
        try {
             line = reader.readLine();
             return line;
        }
        catch( IOException e ) {
             System.out.println("IO Error");
             return null;
        }
    }

    // insert data
    boolean insertInfile( String inputValue ) {
        try {

            this.openFiletoWrite();
            writer.seek ( file.length() );
            writer.writeBytes(inputValue);
            return true;
        }
        catch( IOException e ) {
             System.out.println("IO Error");
             return false;
        }

        finally {
            this.closeWriteFile();

        }
    }

    // delete data when only id is provided
    boolean deletedata ( String inputid ) {

        this.openFiletoRead();
        this.openFiletoWrite();
        
        long currentpos;

        try {

            String line;

            while ( ( line = reader.readLine() ) != null ) {

                if ( line.indexOf( inputid ) == 0 ) {
                    currentpos = reader.getFilePointer();
                    writer.seek ( currentpos - line.length() -1  );
                    writer.writeBytes("$");
                    break;
                }

            }

            return true;

        } catch (FileNotFoundException ex) {
            System.out.print("File Not Found");
            return false;
        } catch (IOException ex) {
            System.out.print("IO Exception Error 1");
            return false;
        }
        
        finally {
            this.closeWriteFile();
            this.closeReadFile();
           
        }

    }
    
         // update data only when id is provided
      boolean updatedata ( String inputid, String newdata ) {

        this.openFiletoRead();
        
        long currentpos;

        try {

            String line;

            while ( ( line = reader.readLine() ) != null ) {

                if ( line.indexOf( inputid ) == 0 ) {
                    this.deletedata(inputid);
                    
                    insertInfile( inputid + ";" + newdata + "\n" );
                    
                    break;

                }

            }

            return true;

        } catch (FileNotFoundException ex) {
            System.out.print("File Not Found");
            return false;
        } catch (IOException ex) {
            System.out.print("IO Exception Error 1");
            return false;
        }

        finally {
            this.closeReadFile();
        }

    }
      
      // get searched line from id
      public String getSearchedLine ( String id ) {

        String line = "";

        this.openFiletoRead();
        while ((line = this.readFromfile()) != null) {

            //System.out.println(line);
            if ( line.equals("")) {
                continue;
            }

            if ( line.indexOf(id) == 0 )
                return line;

        }
        return null;
     }
      
     // get the second value of a csv line
     public String getSecondValue ( String Id ) {

         String result = this.getSearchedLine(Id);
         if (result == null)
             return null;
         String results[] = this.explodeparse(result);

         return results[1];
     }
     
    // get the third value of a csv line
    public String getThirdValue ( String Id ) {

         String result = this.getSearchedLine(Id);
         if (result == null)
             return null;
         String results[] = this.explodeparse(result);

         return results[2];
     }
     
     // explodes the string by the delimiter and returns a string array
    public String[] explodeparse ( String line ) {

        String[] table = line.split(";");
       
        return table;
    }

    // implodes the string by the delimiter ';' and returns a string
    public String implodeparse ( String[] propnames ) {

        String out = "";
        String delim = ";";

        for(int i=0; i<propnames.length; i++) {
        if(i!=0) { out += delim; }
            out += propnames[i];
        }

        return out;

    }

      public Vector<String> GetList()
        {
           this.openFiletoRead();
           Vector<String> li = new Vector();

           String readline;
           while ( (readline=this.readFromfile()) != null ) {
               if ( readline.equals("") || (readline.indexOf("$") == 0 )) {
                   continue;
               }
               li.addElement(this.parseRead(readline));
           }
           this.closeReadFile();
           return li;
        }

      // parse csv  
      public String parseRead(String readline) {
            int semicolonIndex;
            semicolonIndex = readline.indexOf(";");
            return(readline.substring(0,semicolonIndex));
            //System.out.println(readline.substring(semicolonIndex+1,readline.length()));
           }
}
