package com.dowplay.dowplay;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyCrypto {
        static void DecryptExample() throws Exception {
            String payload = "SjZreUFiWmQwTDhtUVpKajA2ZmxyaFBYUDBFY2ZLaFdTN2dCaXNyaysvR2M0RWdPT29TaHdYL25BMjZveFpCQjZCWkJXYTVXb1QvZ2FZSXd6MERodTUrbWh4dy93OUdzUVZHNkxkbk5NbXB2LzFlQlkvWXFhaFBJcFpRRDI3VS9Vc01GSTZ4QnFkNEZOenlOTkhzdmY4dTZoTWtnUGU4Mnc4OW5vRmFXOEowNis4RDlWNG1ibTE1UjJWVTg1WHg3VWpXK0hFNXhiNHp1MnlRUFNpKzArbTBndzBmL0NSd3MrSGRQOVNLS0JSKytzVHFpTFdCVkYrN0VyQ1A2UlEzR0x2ZVRXa3o1Skc4WUpLQzZPZEFnNVpneVYxTEk4bTY1cXZ6SVRFVGs2OHJ0L0o3eUw0dkUxRVFDeVNXWjF4V1lDZytyN2NBY3pHRytVbEtwSmRFT0E0M2RaVmNNblN2WWVTOG50OFU4Y0tKRjJBSHc1bTRaQlNCVDhiQ3Q0TlVadkJhYU1NL0NmQUh3R2lxV1lUcUpNL2U2UzVVdlFTMXErd1ZaMDVNMFFzNjM4UDFSakx4aUxLL3RteEFvWVhPWW5kWFVITkhkTk5MT3hneW9CaW4wU0Vva1pQdHdiV0p5U0tXUVhnWmN6K1QxaUQwMStzN2NOUXRSYVd1S3BHenBHYms1MW4rME9WY1lzb1VsSDNnem10aDVHUjVGVVhQZHlJMGZDR2ZyY3QxODVTWmtJOHNYejQwMFFiVlMvU3RXVzJYRkwrVXp6Nkx1S2ZkLzdRUStvcHJjWGh6Ry9HMUtGa3NTeU45bFM0OWdsQkFqMC9lY0x5U09RWStsbkJSTGlUSWVkdTZYYVhQMy9maUxYZG5BSmMxZUVvQS94K1hQSUlKeDArbm9qNFlxQ1pCaGM3SXJHbFplaFZUQVpaTVZ6R3NBQ21Td2tsT2M3WUNQc2JvZ3hrMDlGZnhpaFM3TEUxdldLUGFmUWlMQkRVeHR4SW1aSStsQXZlYnpQc1YyTW1xbnlyNGlHdytoc0s3ZWptWGdTOWFWYW0vMUM4SERqZ3g1Y1h2elRsSzdXSmY4SUJ1THJCMFpGUlpLOEZqUXBZeDBRZ0w4NU5seEVxamlGZmg5QVRLSWtkMHh2V1lTWnNOV25kSDRNelJ5bVc0b0MyZUUxeGtRSTNYKzBLRzBDL0pDZzlDeXFqMUF5L04xbWtYekNKSzNKeVIydTJMbnBDT28xVHp4V09NcVk0THRuR0VyYm1VT2VvTEdIRWY1NEFUT0ZNTVM1Um9TNDUxbW1mVXU4N2J0ZW0yRWVNQ3pyeU5xL2lVd0xyN0JxTFlZOFV6R29DU3BzQ0VqZkVaTVBOOWZERVFXY1MyVHpxc242VWRLSEh0SEx0eDVmVm1FY1lBWlZRN3E4ZGM9";
            String strPwd = "kYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z";
            String strIv = "6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F)";
            /////////////////////////////////////////////////////
            ///////
            byte[] newIv = Base64.decode(getSHA256(strIv).substring(0,16),Base64.DEFAULT);
            byte[] newKey = Base64.decode(getSHA256(strPwd).substring(0,32),Base64.DEFAULT);
            //byte[] newPayload = payload.getBytes(StandardCharsets.UTF_8);
            String base64Iv = Base64.encodeToString(newIv, Base64.DEFAULT);
            String base64Key = Base64.encodeToString(newKey, Base64.DEFAULT);
            byte[] dataPayload = Base64.decode(payload, Base64.DEFAULT);
            String base64Payload = new String(dataPayload, StandardCharsets.UTF_8);
            Log.d("TestV:", String.valueOf(base64Iv.trim().length()));
            Log.d("TestK:", String.valueOf(base64Key.trim().length()));
            Log.d("TestP:",base64Payload);
            /////////

            IvParameterSpec ivParameterSpec = new IvParameterSpec(base64Iv.trim().getBytes());
            SecretKeySpec secretKey = new SecretKeySpec(base64Key.trim().getBytes(), "AES");
            // Cipher.getInstance("AES/CBC/PKCS5Padding")
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(dataPayload,Base64.DEFAULT));
            String decryptedPayload = new String(decryptedBytes, StandardCharsets.UTF_8);
            System.out.println("TestFinaly: "+decryptedPayload);;
        }

        private static String getSHA256(String input) throws Exception {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
