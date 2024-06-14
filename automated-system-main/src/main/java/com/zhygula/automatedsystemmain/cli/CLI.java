package com.zhygula.automatedsystemmain.cli;

import com.zhygula.automatedsystemmain.exhibition.controller.ExhibitionController;
import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;
import com.zhygula.automatedsystemmain.exhibition.repository.ExhibitionRepository;
import com.zhygula.automatedsystemmain.exhibition.service.ExhibitionService;
import com.zhygula.automatedsystemmain.exhibition.service.impl.ExhibitionServiceImpl;
import com.zhygula.automatedsystemmain.user.controller.UserController;
import com.zhygula.automatedsystemmain.user.model.Role;
import com.zhygula.automatedsystemmain.user.repository.UserRepository;
import com.zhygula.automatedsystemmain.user.service.UserService;
import com.zhygula.automatedsystemmain.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CLI implements CommandLineRunner {

    private final Scanner in = new Scanner(System.in);
    private State state = State.ENTRY;
    private String input = null;
    private int command = 0;
    private long userId = -1;
    private Role role = Role.USER;

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserController userController;
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionService exhibitionService;
    private final ExhibitionController exhibitionController;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello in exhibition application!");
        CLI main = applicationContext.getBean(CLI.class);
        /*
         *
         *
         *
         * */
        while (true) {
            switch (main.state) {
                case ENTRY:
                    main.entryState();
                    break;
                case REGISTER:
                    main.registerState();
                    break;
                case LOGIN:
                    main.loginState();
                    break;
                case AUTHORIZED:
                    main.authorizedState();
                    break;
            }
        }
    }

    private void entryState() {
        System.out.println("""
                Please enter the number of the command you want to execute:
                1. Exhibitions
                2. Register
                3. Login
                4. Exit
                """);
        input = in.nextLine();
        if (!checkCorrectNumberCommand(input, 1, 4)) {
            return;
        }
        switch (command) {
            case 1:
                state = State.AUTHORIZED;
                break;
            case 2:
                state = State.REGISTER;
                break;
            case 3:
                state = State.LOGIN;
                break;
            case 4:
                System.out.println("Thanks for using our application! Bye!");
                System.exit(0);
                break;
        }
    }

    private void registerState() {
        System.out.println("Welcome to the registration form!");
        System.out.println("Please enter your email or enter 0 to return:");
        String email = in.nextLine();
        if (email.equals("0")) {
            state = State.ENTRY;
            return;
        }
        if (userService.checkIsEmailAvailable(email)) {
            System.out.println("Please enter your password:");
            String password = in.nextLine();
            while (password.isEmpty()) {
                System.out.println("Password is empty! Please enter your password:");
                password = in.nextLine();
            }
            System.out.println("Please enter your first name:");
            String firstName = in.nextLine();
            System.out.println("Please enter your last name:");
            String lastName = in.nextLine();
            System.out.println("Please enter your phone number:");
            String phone = in.nextLine();
            userController.register(email, password, firstName, lastName, phone);
            state = State.LOGIN;
        }
    }

    private void loginState() {
        System.out.println("Welcome to the login form!");
        System.out.println("Please enter your email or enter 1 to return:");
        input = in.nextLine();
        if(input.equals("1")){
            state = State.ENTRY;
            return;
        }
        String email = input;
        if (userService.checkIsEmailPresent(email)) {
            System.out.println("Please enter your password:");
            String password = in.nextLine();
            if (userController.login(email, password)) {
                userId = userRepository.findByEmail(email).getId();
                state = State.AUTHORIZED;
                role = userRepository.findByEmail(email).getRole();
            }
        }
    }

    private void authorizedState() {
        System.out.println("Welcome to the exhibitions page!");
        System.out.println("Exhibitions:");
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        printExhibitions(exhibitions);
        switch (role) {
            case USER:
                System.out.println("""
                        Please enter the number of the command you want to execute:
                        1. Filter exhibitions
                        2. Login
                        3. Register
                        4. Back
                        """);
                input = in.nextLine();
                if (!checkCorrectNumberCommand(input, 1, 4)) {
                    return;
                }
                switch (command) {
                    case 1:
                        printFilteredExhibitions(exhibitions);
                        state = State.AUTHORIZED;
                        break;
                    case 2:
                        state = State.LOGIN;
                        break;
                    case 3:
                        state = State.REGISTER;
                        break;
                    case 4:
                        state = State.ENTRY;
                        break;
                }
                break;
            case AUTHORIZED_USER:
                System.out.println("""
                        Please enter the number of the command you want to execute:
                        1. My tickets
                        2. Filter exhibitions
                        3. Buy ticket
                        4. Logout
                        """);
                input = in.nextLine();
                if (!checkCorrectNumberCommand(input, 1, 4)) {
                    return;
                }
                switch (command) {
                    case 1:
                        userController.printTickets(userId);
                        break;
                    case 2:
                        printFilteredExhibitions(exhibitions);
                        break;
                    case 3:
                        System.out.println("Please enter identifier of the exhibition you want to buy ticket or enter 0 to return:");
                        input = in.nextLine();
                        if(input.equals("0")){
                            break;
                        }
                        if (exhibitionController.findById(Long.parseLong(input)) == null) {
                            System.out.println("Invalid exhibition identifier!");
                            return;
                        }
                        userController.buyTicket(userId, Long.parseLong(input));
                        System.out.println("Ticket bought successfully!");
                        state = State.AUTHORIZED;
                        break;
                    case 4:
                        state = State.ENTRY;
                        role = Role.USER;
                        break;
                }

                break;
            case ADMIN:
                System.out.println("""
                        Please enter the number of the command you want to execute:
                        1. Exhibitions
                        2. Add exhibition
                        3. Delete exhibition
                        4. Logout
                        """);
                input = in.nextLine();
                if (!checkCorrectNumberCommand(input, 1, 4)) {
                    return;
                }
                switch (command) {
                    case 1:
                        printExhibitions(exhibitions);
                        state = State.AUTHORIZED;
                        break;
                    case 2:
                        System.out.println("Please enter theme of the exhibition:");
                        String theme = in.nextLine();
                        System.out.println("Please enter halls of the exhibition(hall1, hall2, ... hallLast):");
                        List<String> halls = List.of(in.nextLine().split(", "));
                        System.out.println("Please enter date exhibition starts(year-month-day, for example (2023-01-20)):");
                        String temp = in.nextLine();
                        while (!temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Invalid date format!");
                            System.out.println("Please enter date exhibition starts(year-month-day, for example (2023-01-20)):");
                            temp = in.nextLine();
                        }
                        LocalDate fromDate = LocalDate.parse(temp);
                        System.out.println("Please enter date exhibition ends(year-month-day, for example (2023-10-20)):");
                        temp = in.nextLine();
                        while (!temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Invalid date format!");
                            System.out.println("Please enter date exhibition ends(year-month-day, for example (2023-10-20)):");
                            temp = in.nextLine();
                        }
                        LocalDate toDate = LocalDate.parse(temp);
                        System.out.println("Please enter time exhibition starts(hour:minute, for example (09:00)):");
                        temp = in.nextLine();
                        while (!temp.matches("\\d{2}:\\d{2}")) {
                            System.out.println("Invalid time format!");
                            System.out.println("Please enter time exhibition starts(hour:minute, for example (09:00)):");
                            temp = in.nextLine();
                        }
                        LocalTime fromTime = LocalTime.parse(temp);
                        System.out.println("Please enter time exhibition ends(hour:minute, for example (17:00)):");
                        temp = in.nextLine();
                        while (!temp.matches("\\d{2}:\\d{2}")) {
                            System.out.println("Invalid time format!");
                            System.out.println("Please enter time exhibition ends(hour:minute, for example (17:00)):");
                            temp = in.nextLine();
                        }
                        LocalTime toTime = LocalTime.parse(temp);
                        System.out.println("Please enter price of the exhibition:");
                        temp = in.nextLine();
                        while (!temp.matches("\\d+[.\\d+]?") || Double.parseDouble(temp) < 0) {
                            System.out.println("Invalid price format!");
                            System.out.println("Please enter price of the exhibition:");
                            temp = in.nextLine();
                        }
                        double price = Double.parseDouble(temp);
                        exhibitionController.save(theme, halls, fromDate, toDate, fromTime, toTime, price);
                        System.out.println("Exhibition added successfully!");
                        state = State.AUTHORIZED;
                        break;
                    case 3:
                        for (Exhibition exhibition : exhibitions) {
                            System.out.println(exhibition);
                        }
                        System.out.println("Please enter identifier of the exhibition you want to delete or write back to return:");
                        input = in.nextLine();
                        if(input.equals("back")){
                            break;
                        }
                        if (exhibitionController.findById(Long.parseLong(input)) == null) {
                            System.out.println("Invalid exhibition identifier!");
                            return;
                        }
                        exhibitionController.deleteById(Long.parseLong(input));
                        userController.deleteTicketsByExhibitionId(Long.parseLong(input));
                        System.out.println("Exhibition deleted successfully!");
                        state = State.AUTHORIZED;
                        break;
                    case 4:
                        state = State.ENTRY;
                        role = Role.USER;
                        break;
                }
                break;
        }
    }

    private boolean checkCorrectNumberCommand(String input, int min, int max) {
        try {
            command = Integer.parseInt(input);
            if (command < min || command > max) {
                System.out.println("Invalid command number!");
                command = 0;
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid command number!");
            return false;
        }
        return true;
    }

    private void printFilteredExhibitions(List<Exhibition> exhibitions){
        System.out.println("Please enter filter criteria:");
        System.out.println("""
                                1. Theme
                                2. Price
                                3. Date
                                """);
        input = in.nextLine();
        if (!checkCorrectNumberCommand(input, 1, 3)) {
            return;
        }
        exhibitions = exhibitionController.filter(input);
        printExhibitions(exhibitions);
        System.out.println("Enter something to continue:");
        input = in.nextLine();
    }

    private void printExhibitions(List<Exhibition> exhibitions){
        if(exhibitions.isEmpty()){
            System.out.println("No exhibitions!");
            return;
        }
        for (int i = 1; i <= exhibitions.size(); i++) {
            System.out.println(i + ". " + exhibitions.get(i - 1));
        }
    }
}
