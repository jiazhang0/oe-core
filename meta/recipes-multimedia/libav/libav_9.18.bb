require libav.inc

SRC_URI[md5sum] = "75e838068a75fb88e1b4ea0546bc16f0"
SRC_URI[sha256sum] = "0875e835da683eef1a7bac75e1884634194149d7479d1538ba9fbe1614d066d7"

SRC_URI += "file://libav-fix-CVE-2014-9676.patch \
            file://libav-fix-CVE-2015-1872.patch \
            file://libav-fix-CVE-2015-3395.patch \
            file://libav-fix-CVE-2015-6820.patch \
            file://libav-fix-CVE-2015-6823.patch \
            file://libav-fix-CVE-2015-6824.patch \
            file://libav-fix-CVE-2015-5479.patch \
           "
