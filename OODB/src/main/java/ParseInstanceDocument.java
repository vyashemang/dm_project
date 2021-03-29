import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ParseInstanceDocument {

    public static void main(String[] args) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Employee employee =
                    (Employee) jaxbUnmarshaller.unmarshal(new File("/home/vinayak/Desktop/DM_Project/dm_project/OODB/src/xml_files/single_relation.xml"));
            System.out.println(employee.id + " " + employee.name);
        } catch (Exception ex) {

        }
    }
}
