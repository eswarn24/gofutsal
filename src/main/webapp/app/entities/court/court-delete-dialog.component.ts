import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Court } from './court.model';
import { CourtPopupService } from './court-popup.service';
import { CourtService } from './court.service';

@Component({
    selector: 'jhi-court-delete-dialog',
    templateUrl: './court-delete-dialog.component.html'
})
export class CourtDeleteDialogComponent {

    court: Court;

    constructor(
        private courtService: CourtService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courtService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'courtListModification',
                content: 'Deleted an court'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-court-delete-popup',
    template: ''
})
export class CourtDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtPopupService: CourtPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.courtPopupService
                .open(CourtDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
