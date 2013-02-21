/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.capability;
import java.io.IOException;
/**
 *
 */
public interface DifUpdateCapability {
    /**
     * Execute the update.
     * @return true is an update occurred, false otherwise
     */
    public boolean execute() throws IOException;
