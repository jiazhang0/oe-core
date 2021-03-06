SUMMARY = "System Log Daemons"
DESCRIPTION = "The sysklogd package implements two system log daemons: syslogd, klogd"
HOMEPAGE = "http://www.infodrom.org/projects/sysklogd/"
SECTION = "base"

LICENSE = "GPLv2+ & BSD"
LICENSE_syslogd = "BSD"
LICENSE_klogd = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b \
                    file://syslogd.c;beginline=2;endline=15;md5=77ffb2fec48c46d7ca0abb2d5813e7fd \
                    file://klogd.c;beginline=2;endline=19;md5=7e87ed0ae6142de079bce738c10c899d \
                   "

inherit update-rc.d systemd update-alternatives

SRC_URI = "http://www.infodrom.org/projects/sysklogd/download/sysklogd-${PV}.tar.gz \
           file://no-strip-install.patch \
           file://0001-syslogd.c-add-systemd-support.patch \
           file://sysklogd \
           file://syslog.conf \
           file://sysklogd.service \
           file://klogd.service \
           "

SRC_URI_append_e500v2 = " file://no-vectorization.patch"

INITSCRIPT_NAME = "syslog"
CONFFILES_${PN} = "${sysconfdir}/syslog.conf.${BPN}"

SYSTEMD_SERVICE_${PN} = "sysklogd.service"

CFLAGS_append = " -DSYSV"

do_install () {
	install -d ${D}${mandir}/man8 \
			${D}${mandir}/man5 \
			${D}${base_sbindir}
	oe_runmake 'BINDIR=${D}${base_sbindir}' \
			'MANDIR=${D}${mandir}' install
	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/syslog.conf ${D}${sysconfdir}/syslog.conf
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/sysklogd ${D}${sysconfdir}/init.d/syslog.${BPN}

        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/sysklogd.service ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/klogd.service ${D}${systemd_unitdir}/system
	sed -i -e 's,@BASE_SBINDIR@,${base_sbindir},g' \
		-e 's,@SYSCONFDIR@,${sysconfdir},g' \
		${D}${systemd_unitdir}/system/*.service
}

ALTERNATIVE_PRIORITY = '100'

ALTERNATIVE_${PN} = "syslogd klogd syslog-conf"

ALTERNATIVE_${PN}-doc = "syslogd.8"
ALTERNATIVE_LINK_NAME[syslogd.8] = "${mandir}/man8/syslogd.8"

ALTERNATIVE_LINK_NAME[syslogd] = "${base_sbindir}/syslogd"
ALTERNATIVE_LINK_NAME[klogd] = "${base_sbindir}/klogd"
ALTERNATIVE_LINK_NAME[syslog-conf] = "${sysconfdir}/syslog.conf"

python () {
    if bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        pn = d.getVar('PN', True)
        sysconfdir = d.getVar('sysconfdir', True)
        d.appendVar('ALTERNATIVE_%s' % (pn), ' syslog-init')
        d.setVarFlag('ALTERNATIVE_LINK_NAME', 'syslog-init', '%s/init.d/syslog' % (sysconfdir))
        d.setVarFlag('ALTERNATIVE_TARGET', 'syslog-init', '%s/init.d/syslog.%s' % (d.getVar('sysconfdir', True), d.getVar('BPN', True)))

    if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        pn = d.getVar('PN', True)
        d.appendVar('ALTERNATIVE_%s' % (pn), ' syslog-service')
        d.setVarFlag('ALTERNATIVE_LINK_NAME', 'syslog-service', '%s/systemd/system/syslog.service' % (d.getVar('sysconfdir', True)))
        d.setVarFlag('ALTERNATIVE_TARGET', 'syslog-service', '%s/system/sysklogd.service' % (d.getVar('systemd_unitdir', True)))
}

pkg_prerm_${PN} () {
	if test "x$D" = "x"; then
	if test "$1" = "upgrade" -o "$1" = "remove"; then
		/etc/init.d/syslog stop
	fi
	fi
}
