import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterNewCompanySharedModule } from '../../shared';
import { JhipsterNewCompanyAdminModule } from '../../admin/admin.module';
import {
    PointsService,
    PointsPopupService,
    PointsComponent,
    PointsDetailComponent,
    PointsDialogComponent,
    PointsPopupComponent,
    PointsDeletePopupComponent,
    PointsDeleteDialogComponent,
    pointsRoute,
    pointsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pointsRoute,
    ...pointsPopupRoute,
];

@NgModule({
    imports: [
        JhipsterNewCompanySharedModule,
        JhipsterNewCompanyAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PointsComponent,
        PointsDetailComponent,
        PointsDialogComponent,
        PointsDeleteDialogComponent,
        PointsPopupComponent,
        PointsDeletePopupComponent,
    ],
    entryComponents: [
        PointsComponent,
        PointsDialogComponent,
        PointsPopupComponent,
        PointsDeleteDialogComponent,
        PointsDeletePopupComponent,
    ],
    providers: [
        PointsService,
        PointsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterNewCompanyPointsModule {}
