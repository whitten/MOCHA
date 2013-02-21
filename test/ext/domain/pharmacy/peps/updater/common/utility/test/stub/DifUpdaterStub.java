package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub;
import java.io.File;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
public class DifUpdaterStub extends DifUpdater {
    public int runCount;
    public boolean response;
    public synchronized void runUpdater(File updateFile) {
        System.out.println("Running DIF updater stub for: " + updateFile.getAbsolutePath());
        runCount++;
        if (!response) {
            throw new CommonException(CommonException.RESOURCE_UNAVAILABLE, "bogusfile.xml");
        }
    }
