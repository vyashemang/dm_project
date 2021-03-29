import com.db4o.Db4o;
import com.db4o.ObjectContainer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.util.List;

public class ParseInstanceDocument {

    public static void main(String[] args) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Employee> jaxbElement = jaxbUnmarshaller
                    .unmarshal(new StreamSource("dm_project/OODB/src/DynamicBinding/xmlToJava/resources/xml_files/single_relation.xml"), Employee.class);
            Employee employee = jaxbElement.getValue();
            System.out.println(employee.id + " " + employee.firstName+" "+employee.lastName);

            //persist object into OODB
            ObjectContainer db = Db4o.openFile("Demo");

            try{
                db.store(employee);
            }
            catch (Exception ex) {
                System.out.println(ex.toString());
            }
            finally {
                db.close();
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
