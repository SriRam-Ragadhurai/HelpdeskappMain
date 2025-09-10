package empty1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HelpdeskAppMain {
    public static void main(String[] args) {
        HelpdeskService svc = new HelpdeskService();
        User c1 = svc.registerUser("Alice", "alice@example.com");
        User c2 = svc.registerUser("Bob", "bob@example.com");
        Agent a1 = svc.registerAgent("Charlie", "charlie@help.com", 3);
        Agent a2 = svc.registerAgent("Diana", "diana@help.com", 2);
        Category catNet = svc.createCategory("C1", "Networking", 24, "If breach then escalate to Network Lead");
        Category catApp = svc.createCategory("C2", "Application", 48, "If breach then escalate to App Lead");
        Ticket t1 = svc.openTicket("Cannot connect to VPN", "VPN disconnects frequently", Priority.HIGH, "C1", c1);
        Ticket t2 = svc.openTicket("Login error", "Unable to login to portal", Priority.MEDIUM, "C2", c2);
        List<String> files = new ArrayList<>();
        files.add("screenshot1.png");
        Ticket t3 = svc.openTicket("Payment failure", "Payment gateway times out", Priority.CRITICAL, "C2", c1, files);
        System.out.println("Initial Tickets:");
        t1.display();
        t2.display();
        t3.display();
        System.out.println();
        System.out.println("Assigning tickets to agents:");
        svc.assignTicket(t1.getTicketId(), a1.getId());
        svc.assignTicket(t3.getTicketId(), a1.getId());
        svc.assignTicket(t2.getTicketId(), a2.getId());
        a1.display();
        a2.display();
        System.out.println();
        System.out.println("Move statuses: NEW -> OPEN -> PENDING -> RESOLVED -> CLOSED");
        svc.changeStatus(t1.getTicketId(), Status.OPEN);
        svc.changeStatus(t1.getTicketId(), Status.PENDING);
        svc.changeStatus(t1.getTicketId(), Status.RESOLVED);
        svc.changeStatus(t1.getTicketId(), Status.CLOSED);
        System.out.println("Ticket t1 full details:");
        t1.printFull();
        System.out.println();
        System.out.println("Reassign a critical ticket to another agent (if possible):");
        boolean re = svc.reassignTicket(t3.getTicketId(), a2.getId());
        System.out.println("Reassign success: " + re);
        System.out.println();
        System.out.println("Change priority and try invalid status flow:");
        svc.changePriority(t2.getTicketId(), Priority.HIGH);
        boolean invalid = svc.changeStatus(t2.getTicketId(), Status.CLOSED);
        System.out.println("Attempted to close directly from NEW: allowed? " + invalid);
        System.out.println();
        System.out.println("Simulate SLA breach by manipulating createdAt (for demo only):");
        try {
            java.lang.reflect.Field f = Ticket.class.getDeclaredField("createdAt");
            f.setAccessible(true);
            f.set(t2, LocalDateTime.now().minusDays(3));
        } catch (Exception e) {
        }
        svc.escalateBreaches();
        svc.printDashboard();
        System.out.println();
        System.out.println("Tickets by agent:");
        for (Agent a : new Agent[]{a1, a2}) {
            System.out.println("Agent " + a.getName() + " tickets:");
            for (Ticket tt : svc.listByAgent(a.getId())) tt.display();
        }
        System.out.println();
        System.out.println("Tickets by category:");
        for (Category c : new Category[]{catNet, catApp}) {
            c.display();
            for (Ticket tt : svc.listByCategory(c.getId())) tt.display();
        }
        System.out.println();
        System.out.println("SLA Breached Tickets:");
        for (Ticket tt : svc.slaBreaches()) tt.printFull();
        System.out.println();
        System.out.println("Demonstrating polymorphism: User reference holding Agent");
        User polym = new Agent("A99", "Eve", "eve@help.com", 5);
        polym.display();
    }
}
