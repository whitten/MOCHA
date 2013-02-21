package EXT.DOMAIN.pharmacy.peps.updater.common.capability.test.stub;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.DifUpdateCapability;
public class DifUpdateCapabilityStub implements DifUpdateCapability {
    public boolean didExecute;
    public boolean execute() {
        didExecute = true;
        return true;
    }
