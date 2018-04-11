package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;

public class Tools {

    public Tools(){

    }

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

   public ArrayList<InvalidPosition> getInvalidPositions(String sb) {
        ArrayList<InvalidPosition> invalidPositions= new ArrayList<>();

        // definisci array positions, dove ogni elemento contiene un oggetto json
        sb = sb.substring(1);System.out.println("flusso: " + sb.toString());
        String[] positions = sb.toString().split("},");
        for(String s : positions) {
            System.out.println(s + "---");
        }

        for(int i = 0; i < positions.length; i++) {
            if(i != positions.length - 1) {
                positions[i] = positions[i] + "}";
            }
            else {
                String[] s = positions[i].split("]");
                positions[i] = s[0];
            }
        }

        for(int i= 0; i < positions.length; i++) {
            String a = positions[i].toString();
            ObjectMapper mapper = new ObjectMapper();
            try {
                InvalidPosition pos = mapper.readValue(a, InvalidPosition.class);
                invalidPositions.add(pos);
                //return invalidPositions;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //InvalidPosition invalidPosition= gson.fromJson(a, InvalidPosition.class);

        }
    return invalidPositions;
    }
}
