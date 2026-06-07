import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-account">
        <Translate contentKey="global.menu.entities.userAccount" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient">
        <Translate contentKey="global.menu.entities.sunusantePatientServicePatient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/legal-guardian">
        <Translate contentKey="global.menu.entities.sunusantePatientServiceLegalGuardian" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient-consent">
        <Translate contentKey="global.menu.entities.sunusantePatientServicePatientConsent" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
