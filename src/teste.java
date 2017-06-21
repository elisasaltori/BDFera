import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robson (material original editado: Prof. José Fernando Rodrigues Jr.)
 */
public class teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Statement stmt;
        ResultSet rs;
        Connection connection;
        PreparedStatement pstmt;
        
        try {
            /*CONEXÃO*/
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
                    "8551100",
                    "grupofera");

            /*SELEÇÃO*/
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM MODALIDADEESPORTIVA");
            while (rs.next()) {
                System.out.println(rs.getString("CODIGO") + " - "
                        + rs.getString("NOME") + " - " + rs.getString("DESCRICAO")
                        );
            }

            /*INSERÇÃO*/
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    String s, insert;
            while(true){
                insert = "INSERT INTO";
                System.out.println("Digite SAIR para interromper");
                System.out.println("");
                System.out.println("Digite um nome de tabela:");
                s = bufferRead.readLine();
                if(s.compareTo("SAIR") == 0)
                    break;

                insert += " " + s + " VALUES(";
                rs = stmt.executeQuery("SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH from USER_TAB_COLUMNS where table_name = '" + s + "'");
                while (rs.next()) {
                    System.out.println("Digite um valor para o" +
                                       " atributo " + rs.getString("COLUMN_NAME") +
                                       " do tipo " + rs.getString("DATA_TYPE") +
                                       " de tamanho " + rs.getString("DATA_LENGTH") + ".");
                    s = bufferRead.readLine();

                    if(s.startsWith("empty_"))
                        insert += "" + s + ",";
                    else
                        insert += "'" + s + "',";
                }
                insert = insert.substring(0, insert.length()-1) + ")";
                System.out.println(insert);
                pstmt = connection.prepareStatement(insert);
                try{
                    pstmt.executeUpdate();
                    System.out.println("Dados inseridos");
                    System.out.println("");
                    pstmt.close();
                } catch (SQLException e) {
                  System.out.println("ERRO: dados NÃO inseridos");
                  System.out.println(e.getMessage());
                  System.out.println("tente de novo.");
                }
            }
            stmt.close();
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
