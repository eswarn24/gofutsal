import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CourtLocation } from './court-location.model';
import { CourtLocationPopupService } from './court-location-popup.service';
import { CourtLocationService } from './court-location.service';

@Component({
    selector: 'jhi-court-location-delete-dialog',
    templateUrl: './court-location-delete-dialog.component.html'
})
export class CourtLocationDeleteDialogComponent {

    courtLocation: CourtLocation;

    constructor(
        private courtLocationService: CourtLocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courtLocationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'courtLocationListModification',
                content: 'Deleted an courtLocation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-court-location-delete-popup',
    template: ''
})
export class CourtLocationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtLocationPopupService: CourtLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.courtLocationPopupService
                .open(CourtLocationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
