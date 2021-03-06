import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Court } from '../court//court.model';
import { Booking } from '../booking//booking.model';
import { CourtService } from '../court/court.service';

@Injectable()
export class BookingPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private courtService: CourtService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                console.log("ID from popup service"+id);
                this.courtService.find(id)
                    .subscribe((courtResponse: HttpResponse<Court>) => {
                        const court: Court = courtResponse.body;

                        this.ngbModalRef = this.courtModalRef(component, court, new Booking());
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.courtModalRef(component, new Court(), new Booking());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    courtModalRef(component: Component, court: Court, booking: Booking): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.court = court;
        modalRef.componentInstance.booking=booking;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
