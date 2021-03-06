SUMMARY = "DDR firmware repository"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA.txt;md5=89cc852481956e861228286ac7430d74"

inherit deploy fsl-eula-unpack

SRC_URI = "git://github.com/nxp/ddr-phy-binary.git;fsl-eula=true;nobranch=1 \
    git://source.codeaurora.org/external/qoriq/qoriq-components/atf;nobranch=1;nobranch=1;destsuffix=git/atf;name=atf"
SRCREV = "14d03e6e748ed5ebb9440f264bb374f1280b061c"
SRCREV_atf = "7e34aebe658c7c3439d2d68b0ce6b9776e8e6996"

S = "${WORKDIR}/git"

REGLEX_lx2160a = "lx2160a"

do_install () {
    oe_runmake -C ${S}/atf fiptool
    cd ${S}/${REGLEX}
    ${S}/atf/tools/fiptool/fiptool create --ddr-immem-udimm-1d ddr4_pmu_train_imem.bin \
    --ddr-immem-udimm-2d ddr4_2d_pmu_train_imem.bin \
    --ddr-dmmem-udimm-1d ddr4_pmu_train_dmem.bin \
    --ddr-dmmem-udimm-2d ddr4_2d_pmu_train_dmem.bin \
    --ddr-immem-rdimm-1d ddr4_rdimm_pmu_train_imem.bin \
    --ddr-immem-rdimm-2d ddr4_rdimm2d_pmu_train_imem.bin \
    --ddr-dmmem-rdimm-1d ddr4_rdimm_pmu_train_dmem.bin \
    --ddr-dmmem-rdimm-2d ddr4_rdimm2d_pmu_train_dmem.bin \
    fip_ddr_all.bin
    install -d ${D}/boot
    install -m 755 ${S}/${REGLEX}/*.bin ${D}/boot
}

do_deploy () {
    install -d ${DEPLOYDIR}/ddr-phy
    install -m 755 ${S}/${REGLEX}/*.bin ${DEPLOYDIR}/ddr-phy
}
addtask deploy before do_populate_sysroot after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot"

COMPATIBLE_MACHINE = "(lx2160a)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
