package org.adrianwalker.fhir.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * Patient Example xml from: https://www.hl7.org/fhir/patient-example.xml.html
 */
public final class PatientExampleTest {

  private static Unmarshaller unmarshaller;
  private static Marshaller marshaller;

  @BeforeClass
  public static void setUp() throws JAXBException {

    JAXBContext context = JAXBContext.newInstance(Patient.class);
    unmarshaller = context.createUnmarshaller();
    marshaller = context.createMarshaller();
  }

  @Test
  public void testXmlToPatient() throws JAXBException {

    Patient patient = unmarshalPatient("src/test/resources/patient-example.xml");

    Assert.assertEquals("example", patient.getId().getValue());
    Assert.assertEquals("Chalmers", patient.getName().get(0).getFamily().getValue());
    Assert.assertEquals("Peter", patient.getName().get(0).getGiven().get(0).getValue());
    Assert.assertEquals("James", patient.getName().get(0).getGiven().get(1).getValue());
  }

  @Test
  public void testPatientToXml() throws JAXBException {

    Patient patient = new Patient()
            .withId(new Id().withValue("test"))
            .withName(new HumanName()
                    .withGiven(new String().withValue("Adrian"))
                    .withFamily(new String().withValue("Walker")));

    Assert.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<Patient xmlns=\"http://hl7.org/fhir\" xmlns:ns2=\"http://www.w3.org/1999/xhtml\">"
            + "<id value=\"test\"/>"
            + "<name>"
            + "<family value=\"Walker\"/>"
            + "<given value=\"Adrian\"/>"
            + "</name>"
            + "</Patient>",
            marshalPatient(patient));
  }

  private Patient unmarshalPatient(final java.lang.String filename) throws JAXBException {

    JAXBElement<Patient> element = unmarshaller.unmarshal(
            new StreamSource(new File(filename)), Patient.class);

    return element.getValue();
  }

  private java.lang.String marshalPatient(final Patient patient) throws JAXBException {

    JAXBElement<Patient> element = new ObjectFactory().createPatient(patient);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    marshaller.marshal(element, baos);

    return baos.toString();
  }
}
