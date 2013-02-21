/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckMessageVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import dbank.dif.Message;
import dbank.dif.Messages;
import dbank.dif.ScreenDrugs;
/**
 * Convert Messages object into Collection of DrugCheckMessageVO
 */
public class MessageConversionUtility {
    /**
     * Convert Messages object into Collection of DrugCheckMessageVO
     * 
     * @param messages Messages to convert to DrugCheckMessageVOs
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param screenDrugs ScreenDrugs object originally sent for screening, used to get the drug ID
     * @return Collection of DrugCheckMessageVO
     */
    public static Collection<DrugCheckMessageVo> toDrugCheckMessages(Messages messages,
                                                                                Map<String, DrugCheckVo> drugMap,
                                                                                ScreenDrugs screenDrugs) {
        Collection<DrugCheckMessageVo> list = new ArrayList<DrugCheckMessageVo>(messages.count());
        for (int i = 0; i < messages.count(); i++) {
            Message message = messages.item(i);
            DrugCheckMessageVo messageVO = new DrugCheckMessageVo();
            messageVO.setDrug(drugMap.get(screenDrugs.get(message.getDrugIndex()).getCustomID()));
            messageVO.setDrugName(StringUtility.nullToEmptyString(message.getDrugDescription()));
            messageVO.setSeverity(StringUtility.nullToEmptyString(MessageSeverityUtility.convert(message.getSeverity())));
            messageVO.setText(StringUtility.nullToEmptyString(message.getMessageText()));
            messageVO.setType(StringUtility.nullToEmptyString(MessageTypeUtility.convert(message.getMessageType())));
            list.add(messageVO);
        }
        return list;
    }
    /**
     * Cannot instantiate
     */
    private MessageConversionUtility() {
        super();
    }
