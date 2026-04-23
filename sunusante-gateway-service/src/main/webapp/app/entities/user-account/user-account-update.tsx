import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUserAccount } from 'app/shared/model/user-account.model';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';
import { getEntity, updateEntity, createEntity, reset } from './user-account.reducer';

export const UserAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userAccountEntity = useAppSelector(state => state.sunusantegatewayservice.userAccount.entity);
  const loading = useAppSelector(state => state.sunusantegatewayservice.userAccount.loading);
  const updating = useAppSelector(state => state.sunusantegatewayservice.userAccount.updating);
  const updateSuccess = useAppSelector(state => state.sunusantegatewayservice.userAccount.updateSuccess);
  const userRoleValues = Object.keys(UserRole);

  const handleClose = () => {
    navigate('/user-account' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...userAccountEntity,
      ...values,
      internalUser: users.find(it => it.id.toString() === values.internalUser?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          role: 'ROLE_PATIENT',
          ...userAccountEntity,
          createdDate: convertDateTimeFromServer(userAccountEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userAccountEntity.lastModifiedDate),
          internalUser: userAccountEntity?.internalUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sunusanteGatewayServiceApp.userAccount.home.createOrEditLabel" data-cy="UserAccountCreateUpdateHeading">
            <Translate contentKey="sunusanteGatewayServiceApp.userAccount.home.createOrEditLabel">Create or edit a UserAccount</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-account-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.mfaSecret')}
                id="user-account-mfaSecret"
                name="mfaSecret"
                data-cy="mfaSecret"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.role')}
                id="user-account-role"
                name="role"
                data-cy="role"
                type="select"
              >
                {userRoleValues.map(userRole => (
                  <option value={userRole} key={userRole}>
                    {translate('sunusanteGatewayServiceApp.UserRole.' + userRole)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.createdBy')}
                id="user-account-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.createdDate')}
                id="user-account-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.lastModifiedBy')}
                id="user-account-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.userAccount.lastModifiedDate')}
                id="user-account-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-account-internalUser"
                name="internalUser"
                data-cy="internalUser"
                label={translate('sunusanteGatewayServiceApp.userAccount.internalUser')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-account" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserAccountUpdate;
