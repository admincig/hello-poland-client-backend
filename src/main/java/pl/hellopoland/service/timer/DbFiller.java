package pl.hellopoland.service.timer;

import pl.hellopoland.bo.*;
import pl.hellopoland.bo.Portal.Type;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.*;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.service.*;
import pl.hellopoland.util.DtoMapper;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.lang.System.Logger;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Startup
@Singleton
@DependsOn({"Configuration"})
@ApplicationScoped
public class DbFiller extends ServiceSuperclass {

  @Inject
  CategoryService categoryService;
  @Inject
  TagService tagService;

  private Random random = new Random();

  @PostConstruct
  public void fillDb() {
    Boolean run = Boolean.valueOf(properties.getProperty("db.filler.run"));
    if (!run) {
      logger.log(Logger.Level.INFO, "db filler disabled");
      return;
    }
    logger.log(Logger.Level.INFO, "DbFiller started");
    logger.log(Logger.Level.INFO,
        "OS architecture: " + System.getProperty("os.arch").toLowerCase());
    logger.log(Logger.Level.INFO, "OS name: " + System.getProperty("os.name").toLowerCase());
    logger.log(Logger.Level.INFO, "Envi: " + System.getenv("ProgramFiles(x86)"));
    createPortals();
    createUsers();
    createCategories();
    createTags();
    logger.log(Logger.Level.INFO, "dbfiller finished");
  }

  private void createCategories() {
    for (int i = 0; i < 10; i++) {
      createCategory("kategoria " + i);
    }
  }

  private void createTags() {
    for (int i = 0; i < 10; i++) {
      createTag("tag " + i);
    }
  }

  private void createCategory(String label) {
    CategoryDTO dto = new CategoryDTO();
    dto.label = label;
    dto.recommended = random.nextBoolean();
    dto.restricted = random.nextBoolean();
    dto.language = "pl-pl";
    dto.iconUrl = "https://static.thenounproject.com/png/22802-200.png";
    categoryService.create(dto);
  }

  private void createTag(String label) {
    TagDTO dto = new TagDTO();
    dto.label = label;
    dto.recommended = random.nextBoolean();
    dto.restricted = random.nextBoolean();
    dto.language = "pl-pl";
    dto.iconUrl = "https://static.thenounproject.com/png/22802-200.png";
    tagService.create(dto);
  }

  private void createUsers() {
    createUser("Hello Poland - admin", "admin@hello-poland.pl", "uEXVMJeQKGJ246ARHRbKBGc6",
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBoZWxsby1wb2xhbmQucGwiLCJhdXRoIjoiUk9MRV9BRE1JTiJ9.wrXkY3YB_y4Vs-ADKbixCbLLAQ5G8apSlwPuYXp6DcnXMLaBrPA8GFeGG9_uaA4fP26fkbstbXwR6OnW30GQeA",
        null, Role.ADMIN, Role.ROOT);
    createUser("Hello Poland - salesman", "salesman@hello-poland.pl", "uEXVMJeQKGJ246ARHRbKBGc6",
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWxlc21hbkBoZWxsby1wb2xhbmQucGwiLCJhdXRoIjoiUk9MRV9BRE1JTiJ9._nXw8sT0ZJ77Ac-RdS-zrrA40HJjDXtTyVNSq-VZns5JSyy5JSXb7j_qEPy_XuO4Y-P1FGYoKfVQcuxGWOFFkA",
        null, Role.SALESMAN);
  }

  private User createUser(String name, String email, String password, String hptToken,
      Partner partner, Role... roles) {
    User user = new User(roles);
    user.setDetails(new UserDetails(name, null));
    user.setEmail(email);
    user.changePassword(password);
    user.setPartner(partner);
    user.setHptToken(hptToken);
    em.persist(user);
    return user;
  }

  private void createPortals() {
    createHelloTicketCloud();
  }

  private void createHelloTicketCloud() {
    Portal portal = new Portal();
    portal.setName("Hello Ticket Cloud");
    portal.setUrl(properties.getProperty("hpt.cloud.url"));
    portal.setType(Type.HELLOTICKET_CLOUD_1);
    em.persist(portal);
  }

}
