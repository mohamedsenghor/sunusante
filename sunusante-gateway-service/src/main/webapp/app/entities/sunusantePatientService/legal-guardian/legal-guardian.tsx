import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './legal-guardian.reducer';

export const LegalGuardian = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const legalGuardianList = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.entities);
  const loading = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.loading);
  const totalItems = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="legal-guardian-heading" data-cy="LegalGuardianHeading">
        <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.title">Legal Guardians</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.refreshListLabel">
              Refresh List
            </Translate>
          </Button>
          <Link to="/legal-guardian/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.createLabel">
              Create new Legal Guardian
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {legalGuardianList && legalGuardianList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('guardianType')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.guardianType">
                    Guardian Type
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('guardianType')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdDate">
                    Created Date
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedBy">
                    Last Modified By
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedDate">
                    Last Modified Date
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.dependent">Dependent</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {legalGuardianList.map((legalGuardian, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/legal-guardian/${legalGuardian.id}`} color="link" size="sm">
                      {legalGuardian.id}
                    </Button>
                  </td>
                  <td>{legalGuardian.guardianType}</td>
                  <td>{legalGuardian.createdBy}</td>
                  <td>
                    {legalGuardian.createdDate ? (
                      <TextFormat type="date" value={legalGuardian.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{legalGuardian.lastModifiedBy}</td>
                  <td>
                    {legalGuardian.lastModifiedDate ? (
                      <TextFormat type="date" value={legalGuardian.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {legalGuardian.dependent ? <Link to={`/patient/${legalGuardian.dependent.id}`}>{legalGuardian.dependent.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/legal-guardian/${legalGuardian.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/legal-guardian/${legalGuardian.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/legal-guardian/${legalGuardian.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.notFound">
                No Legal Guardians found
              </Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={legalGuardianList && legalGuardianList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default LegalGuardian;
