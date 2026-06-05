package com.customer_service;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.customer_service.api.dto.DtoCustomerImageIn;
import com.customer_service.api.dto.DtoCustomerIn;
import com.customer_service.api.dto.DtoRegionIn;
import com.customer_service.api.entity.Customer;
import com.customer_service.api.entity.CustomerImage;
import com.customer_service.api.entity.Region;
import com.customer_service.api.repository.RepoCustomer;
import com.customer_service.api.repository.RepoCustomerImage;
import com.customer_service.api.repository.RepoRegion;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CustomerServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepoRegion repoRegion;

    @Autowired
    private RepoCustomer repoCustomer;

    @Autowired
    private RepoCustomerImage repoCustomerImage;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String testSecret;

    private String customerToken;
    private String adminToken;

    private Region activeRegion;
    private Region inactiveRegion;

    @BeforeEach
    void setup() {
        repoCustomerImage.deleteAll();
        repoCustomer.deleteAll();
        repoRegion.deleteAll();

        // Generate tokens matching auth-service roles
        customerToken = generateToken("customer_user", 1, List.of("User"));
        adminToken = generateToken("admin_user", 99, List.of("Administrator"));

        // Setup base regions
        activeRegion = new Region(null, "North Region", "NOR", 1);
        activeRegion = repoRegion.save(activeRegion);

        inactiveRegion = new Region(null, "South Region", "SOU", 0);
        inactiveRegion = repoRegion.save(inactiveRegion);
    }

    private String generateToken(String username, Integer id, List<String> roles) {
        Key key = Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // @spec CUST-REG-001
    @Test
    void testFindAllRegions() throws Exception {
        mockMvc.perform(get("/region"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // @spec CUST-REG-002
    @Test
    void testFindActiveRegions() throws Exception {
        mockMvc.perform(get("/region/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].region", is("North Region")));
    }

    // @spec CUST-REG-003
    @Test
    void testCreateRegionSuccess() throws Exception {
        DtoRegionIn in = new DtoRegionIn();
        in.setRegion("East Region");
        in.setTag("EAS");

        mockMvc.perform(post("/region")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk());

        List<Region> regions = repoRegion.findAll();
        assertEquals(3, regions.size());
    }

    // @spec CUST-REG-003
    @Test
    void testCreateRegionForbiddenForCustomer() throws Exception {
        DtoRegionIn in = new DtoRegionIn();
        in.setRegion("West Region");
        in.setTag("WES");

        mockMvc.perform(post("/region")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isForbidden());
    }

    // @spec CUST-REG-004
    @Test
    void testUpdateRegionSuccess() throws Exception {
        DtoRegionIn in = new DtoRegionIn();
        in.setRegion("North Region Updated");
        in.setTag("NRU");

        mockMvc.perform(put("/region/" + activeRegion.getRegion_id())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk());

        Region updated = repoRegion.findById(activeRegion.getRegion_id()).orElseThrow();
        assertEquals("North Region Updated", updated.getRegion());
        assertEquals("NRU", updated.getTag());
    }

    // @spec CUST-REG-005
    @Test
    void testEnableRegion() throws Exception {
        mockMvc.perform(patch("/region/" + inactiveRegion.getRegion_id() + "/enable")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        Region updated = repoRegion.findById(inactiveRegion.getRegion_id()).orElseThrow();
        assertEquals(1, updated.getStatus());
    }

    // @spec CUST-REG-006
    @Test
    void testDisableRegion() throws Exception {
        mockMvc.perform(patch("/region/" + activeRegion.getRegion_id() + "/disable")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        Region updated = repoRegion.findById(activeRegion.getRegion_id()).orElseThrow();
        assertEquals(0, updated.getStatus());
    }

    // @spec CUST-PROFILE-001
    @Test
    void testFindAllCustomers() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 1);
        repoCustomer.save(cust);

        mockMvc.perform(get("/customer")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // @spec CUST-PROFILE-002
    @Test
    void testGetCustomerDetail() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 1);
        cust = repoCustomer.save(cust);
        
        repoCustomerImage.save(new CustomerImage(null, cust.getCustomer_id(), "img/test.png", 1));

        mockMvc.perform(get("/customer/" + cust.getCustomer_id())
                .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.region", is("North Region")));
    }

    // @spec CUST-PROFILE-003
    @Test
    void testRegisterCustomerSuccess() throws Exception {
        DtoCustomerIn in = new DtoCustomerIn();
        in.setName("Alice");
        in.setSurname("Smith");
        in.setRfc("SMIA900101ABC");
        in.setMail("alice.smith@mail.com");
        in.setPhone_number("12345678901");
        in.setAddress("456 Ave");
        in.setUser_id(2);
        in.setRegion_id(activeRegion.getRegion_id());

        mockMvc.perform(post("/customer")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk());

        List<Customer> customers = repoCustomer.findAll();
        assertEquals(1, customers.size());
        assertEquals("Alice", customers.get(0).getName());
    }

    // @spec CUST-PROFILE-003
    @Test
    void testRegisterCustomerFailsWithInactiveRegion() throws Exception {
        DtoCustomerIn in = new DtoCustomerIn();
        in.setName("Alice");
        in.setSurname("Smith");
        in.setRfc("SMIA900101ABC");
        in.setMail("alice.smith@mail.com");
        in.setPhone_number("12345678901");
        in.setAddress("456 Ave");
        in.setUser_id(2);
        in.setRegion_id(inactiveRegion.getRegion_id()); // Inactive region

        mockMvc.perform(post("/customer")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isBadRequest());
    }

    // @spec CUST-PROFILE-004
    @Test
    void testUpdateCustomerSuccess() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 1);
        cust = repoCustomer.save(cust);

        DtoCustomerIn in = new DtoCustomerIn();
        in.setName("John Updated");
        in.setSurname("Doe");
        in.setRfc("DOEJ900101XYZ");
        in.setMail("john.doe.updated@mail.com");
        in.setPhone_number("12345678901");
        in.setAddress("789 Lane");
        in.setUser_id(1);
        in.setRegion_id(activeRegion.getRegion_id());

        mockMvc.perform(put("/customer/" + cust.getCustomer_id())
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk());

        Customer updated = repoCustomer.findById(cust.getCustomer_id()).orElseThrow();
        assertEquals("John Updated", updated.getName());
        assertEquals("john.doe.updated@mail.com", updated.getMail());
    }

    // @spec CUST-PROFILE-005
    @Test
    void testEnableCustomer() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 0);
        cust = repoCustomer.save(cust);

        mockMvc.perform(patch("/customer/" + cust.getCustomer_id() + "/enable")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        Customer updated = repoCustomer.findById(cust.getCustomer_id()).orElseThrow();
        assertEquals(1, updated.getStatus());
    }

    // @spec CUST-PROFILE-006
    @Test
    void testDisableCustomer() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 1);
        cust = repoCustomer.save(cust);

        mockMvc.perform(patch("/customer/" + cust.getCustomer_id() + "/disable")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        Customer updated = repoCustomer.findById(cust.getCustomer_id()).orElseThrow();
        assertEquals(0, updated.getStatus());
    }

    // @spec CUST-IMAGE-001
    @Test
    void testUploadCustomerImageSuccess() throws Exception {
        Customer cust = new Customer(null, "John", "Doe", "DOEJ900101XYZ", "john.doe@mail.com", "+12345678901", "123 St", 1, activeRegion.getRegion_id(), 1);
        cust = repoCustomer.save(cust);

        DtoCustomerImageIn in = new DtoCustomerImageIn();
        in.setCustomerId(cust.getCustomer_id());
        in.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");

        mockMvc.perform(post("/customer-image")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(in)))
                .andExpect(status().isOk());

        assertEquals(1, repoCustomerImage.findAll().size());
    }
}
