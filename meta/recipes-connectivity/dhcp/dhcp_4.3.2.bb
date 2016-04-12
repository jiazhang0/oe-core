require dhcp.inc

SRC_URI += "file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
            file://fixsepbuild.patch \
            file://dhclient-script-drop-resolv.conf.dhclient.patch \
            file://replace-ifconfig-route.patch \
            file://dhcp-add-exec-script-function.patch \
            file://0001-site.h-enable-gentle-shutdown.patch \
            file://0001-master-Clean-up-packet-handling.patch \
            file://CVE-2016-2774.patch \
           "

SRC_URI[md5sum] = "5a284875dd2c12ddd388416d69156a67"
SRC_URI[sha256sum] = "6246c9b358759f6cdcc45104caaf76e732a211dbbbbf64a21f499c8db1298165"
