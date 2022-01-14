//package com.bmts.heating.commons.basement.services.ldap;
//
//import com.bmts.heating.commons.basement.utils.LdapStrUtil;
//import com.bmts.heating.commons.utils.msmq.PointL;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.ldap.core.DirContextAdapter;
//
//import javax.naming.NamingException;
//import javax.naming.directory.Attribute;
//import javax.naming.directory.Attributes;
//import java.util.List;
//
//public class PointLCommon {
//
//    public static void setListDto(DirContextAdapter context, List<PointL> list) throws NamingException {
//        Attributes attr = context.getAttributes();
//        PointL pointl = new PointL();
//        String dn = context.getDn().toString();
//        if (StringUtils.isEmpty(dn)) {
//            return;
//        }
//        pointl.setPointId(dn);
//        String[] split = dn.split(",");
//        pointl.setHeatingStationId(split[split.length - 1]);
//
//
//        Attribute ou = attr.get("ou");
//        if (ou != null) {
//            pointl.setPointName(ou.get().toString());
//        }
//        Attribute comTsccExpandDesc = attr.get("comTsccExpandDesc");
//        if (comTsccExpandDesc != null) {
//            pointl.setExpandDesc(comTsccExpandDesc.get().toString());
//        }
//
//        Attribute comTsccPointAddress = attr.get("comTsccPointAddress");
//        if (comTsccPointAddress != null) {
//            pointl.setPointAddress(comTsccPointAddress.get().toString());
//        }
//
//        Attribute comTsccPointDevice = attr.get("comTsccPointDevice");
//        if (comTsccPointDevice != null) {
//            pointl.setDeviceId(comTsccPointDevice.get().toString());
//        }
//
//        Attribute comTsccPointName = attr.get("comTsccPointName");
//        if (comTsccPointName != null) {
//            pointl.setPointName(comTsccPointName.get().toString());
//        }
//
//        Attribute comTsccPointType = attr.get("comTsccPointType");
//        if (comTsccPointType != null && LdapStrUtil.isNumeric(comTsccPointType.get().toString())) {
//            pointl.setType(Integer.parseInt(comTsccPointType.get().toString()));
//        }
//
//        Attribute comTsccPointWashPolicy = attr.get("comTsccPointWashPolicy");
//        if (comTsccPointWashPolicy != null) {
//            int[] arr = new int[3];
//            arr[0] = 1;
//            arr[1] = 1;
//            arr[2] = 3;
//            pointl.setWashArray(arr);
//        }
//
//        list.add(pointl);
//    }
//
//}
