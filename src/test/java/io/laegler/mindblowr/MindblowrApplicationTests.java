package io.laegler.mindblowr;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.io.InputStream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = MindblowrApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yml")
public class MindblowrApplicationTests {

	@Autowired
	private MockMvc mvc;

	// @Autowired
	// private WebApplicationContext webApplicationContext;

	// @Autowired
	// private EmployeeRepository repository;

	@Before
	public void setup() throws Exception {
		// this.mvc = webAppContextSetup(webApplicationContext).build();
		//
		// this.bookmarkRepository.deleteAllInBatch();
		// this.accountRepository.deleteAllInBatch();
		//
		// this.account = accountRepository.save(new Account(userName, "password"));
		// this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" +
		// userName, "A description")));
		// this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" +
		// userName, "A description")));
	}

	@Test
	public void test1() throws Exception {
		mvc//
				.perform(//
						get("/actuator/health").contentType(APPLICATION_JSON))//
				.andDo(log())//
				.andExpect(//
						status().isOk())//
				.andExpect(//
						content().contentTypeCompatibleWith("application/vnd.spring-boot.actuator.v2+json;charset=UTF-8"))
				// .andExpect(//
				// jsonPath("$[0].name", is("bob")))
				.andReturn().getResponse().toString();
	}

	@Test
	public void test2() throws Exception {
		mvc//
				.perform(//
						multipart("/mind/blow")//
								.file(new MockMultipartFile("file", getTestFileInputStream()))//
								.param("file", "4"))
				.andDo(log())//
				.andExpect(//
						status().isOk())//
				.andExpect(//
						content().contentTypeCompatibleWith(APPLICATION_OCTET_STREAM_VALUE))//
				// .andExpect(//
				// jsonPath("$[0].name", is("bob")))//
				.andReturn().getResponse().toString();
	}

	@Test
	public void test3() throws Exception {
		mvc//
				.perform(//
						multipart("/mind/blow")//
								.file(new MockMultipartFile("file", getTestFileInputStream()))//
								.param("file", "4"))
				.andDo(log())//
				.andExpect(//
						status().isOk())//
				.andExpect(//
						content().contentTypeCompatibleWith(APPLICATION_OCTET_STREAM_VALUE))//
				// .andExpect(//
				// jsonPath("$[0].name", is("bob")))
				.andReturn().getResponse().toString();
	}

	// @Test
	// public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
	//
	// Employee alex = new Employee("alex");
	//
	// List<Employee> allEmployees = Arrays.asList(alex);
	//
	// given(service.getAllEmployees()).willReturn(allEmployees);
	//
	// mvc.perform(get("/api/employees").contentType(APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$",
	// hasSize(1)))
	// .andExpect(jsonPath("$[0].name", is(alex.getName())));
	// }

	@SuppressWarnings("null")
	private InputStream getTestFileInputStream() {
		// ClassLoader classLoader = getClass().getClassLoader();
		// URL url = classLoader.getResource("test.docx");
		// File file = new File(url.getFile());
		// InputStream inputStream = null;
		// try {
		return this.getClass().getClassLoader().getResourceAsStream("/test.docx");
		// inputStream = new FileInputStream(file);
		// } catch (FileNotFoundException e1) {
		// log.error("Failed to read input file: {} (docx)", "test.docx", e1);
		// try {
		// inputStream.close();
		// } catch (IOException e2) {
		// log.error("Failed to read input file: {} (docx)", file.getName(), e2);
		// }
		// }
		// return inputStream;
	}

}
