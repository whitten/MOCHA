package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.VerificationTester;
public class VerificationTesterStub extends VerificationTester {
    public int runCount;
    public boolean response;
    public int failOnRunCount = -1;
    public boolean executeOrderCheckTests() {
        runCount++;
        if (failOnRunCount == runCount) {
            return false;
        }
        return response;
    }
