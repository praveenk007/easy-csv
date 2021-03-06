import com.easycsv.utils.CSVByteUtils;
import com.easycsv.utils.ZipUtils;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CsvTest {

    public static void main(String args[]) {



        CSVByteUtils t = new CSVByteUtils(",");

        PolicyDetails pd1 = new PolicyDetails();
        pd1.setfName("praveen");
        pd1.setlName("kamath");
        pd1.setPolicyNum("12349494");
        pd1.setAge(20);
        List<String> previousPolicies = new ArrayList<>();
        previousPolicies.add("pol1");
        previousPolicies.add("pol2");
        previousPolicies.add("pol3");
        previousPolicies.add("pol4");
        String[] diseases = {"dis1", "dis2"};
        int[] amounts = {1144, 2222};
        Float[] floats = {1.2f, 23.6f};
        pd1.setFloaters(floats);
        pd1.setDiseases(diseases);
        pd1.setAmounts(amounts);
        pd1.setPreviousPolicies(previousPolicies);
        List<Integer> ages = new ArrayList<>();
        ages.add(10);
        ages.add(20);
        ages.add(30);
        Member m1 = new Member();
        MemberAddress add = new MemberAddress();
        add.setLine1("line1 address");
        add.setLine2("line2 address");
        m1.setAddress(add);
        m1.setFname("praveen");
        m1.setLname("kamath");
        m1.setContactNumber("9869390062");

        Member[] memberArray = {m1};
        pd1.setMemberArray(memberArray);

        List<Member> members = new ArrayList();
        members.add(m1);
        pd1.setMemebers(members);
        pd1.setAges(ages);

        List<Double> policyPremiums = new ArrayList<>();
        policyPremiums.add(10.0);
        policyPremiums.add(20.1);
        policyPremiums.add(30.2);
        pd1.setPolicyPremiums(policyPremiums);

        PolicyDetails pd2 = new PolicyDetails();
        pd2.setfName("Beer");
        pd2.setlName("Pratap");
        pd2.setPolicyNum("47474747");
        pd2.setAge(23);
        List<String> previousPolicies2 = new ArrayList<>();
        previousPolicies2.add("pol1");
        previousPolicies2.add("pol2");
        previousPolicies2.add("pol3");
        previousPolicies2.add("pol4");
        pd2.setPreviousPolicies(previousPolicies2);


        List list = new ArrayList();
        list.add(pd1);
        //list.add(pd2);

        FileWriter fw = null;
        try {
            byte[] b = t.write(list, true);

            fw = new FileWriter(new File("/Users/praveenkamath/Documents/test.csv"));
            String sb = new String(Base64.getDecoder().decode(ZipUtils.decompress(b).toString()));
            fw.write(sb);
            fw.flush();
            System.out.println("File written");
            System.out.println(sb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
