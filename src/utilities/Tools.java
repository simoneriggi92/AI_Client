package utilities;

import com.company.Position;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    public Tools(){}

   public String get_MD5_Password(String passwordToHash) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(passwordToHash.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

   public void mapInvalidPositions(String sb) {
       List<InvalidPosition> invalidList = null;
       ObjectMapper mapper = new ObjectMapper();

       try {
           invalidList = mapper.readValue(sb, mapper
                   .getTypeFactory()
                   .constructCollectionType(List.class, InvalidPosition.class));
           System.out.println("(/Client)#Invalid positions: " + invalidList.size());
           for(InvalidPosition i : invalidList){
               System.out.println("(/Client)REJECTED: ");
               System.out.println("description: wrong -> "+i.getDescription());
               System.out.println("latitude"+i.getLatitude());
               System.out.println("longitude"+i.getLongitude());
               System.out.println("timestamp"+i.getTimestamp());
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

   public void mapRangePositions(String sb){
       ArrayList<Position> positionList = null;
       ObjectMapper mapper = new ObjectMapper();

       try {

          positionList = mapper.readValue(sb, mapper
                   .getTypeFactory()
                   .constructCollectionType(ArrayList.class, Position.class));
           System.out.println("(/Client)#Returned positions: " + positionList.size());
           for(Position p : positionList){
               System.out.println("(/Client)POS: ");
               System.out.println("latitude"+p.getLatitude());
               System.out.println("longitude"+p.getLongitude());
               System.out.println("timestamp"+p.getTimestamp());
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

}
